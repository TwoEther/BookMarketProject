package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.*;
import org.project.shop.service.*;
import org.project.shop.web.ItemForm;
import org.project.shop.web.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    private final InquiryServiceImpl inquiryServiceImpl;

    @PostMapping(value = "/dbConfig")
    public String dbConfig(Model model) throws Exception {
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        String path1 = "C:\\lee\\Java\\data.csv";
        String path2 = "C:\\lee\\Project\\Spring\\data.csv";
        String imagePath1 = "C:\\lee\\Java\\bookImages\\";
        String imagePath2 = "C:\\lee\\Project\\Spring\\bookImages\\";

        try{
            br = Files.newBufferedReader(Paths.get(path1));
            String line = "";

            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);
//                System.out.println(tmpList);
                ret.add(tmpList);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        for (List<String> data : ret) {
            String title = data.get(0);
            int price = Integer.parseInt(data.get(1));
            int stockQuantity = Integer.parseInt(data.get(2));
            String author = data.get(3);
            String publisher = data.get(4);
            int isbn = Integer.parseInt(data.get(5));
            int page = Integer.parseInt(data.get(6));
            String description = data.get(7);
            String fileName = data.get(8);
            String category1 = data.get(9);
            String category2 = data.get(10);

            if (categoryServiceImpl.findByCategoryName(category1, category2) == null) {
                categoryServiceImpl.save(new Category(category1, category2));
            }
            Category findCategory = categoryServiceImpl.findByCategoryName(category1, category2);

            String fileRoot = imagePath1 + fileName+".png";
            Item item = new Item(title, price, stockQuantity, author, publisher, isbn, page, description);
            item.setCategory(findCategory);
            File imageFile = new File(fileRoot);
            BufferedImage image = ImageIO.read(imageFile);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos );
            baos.flush();
            MultipartFile multipartFile = new MockMultipartFile(fileName, baos.toByteArray());
            itemServiceImpl.saveItem(item, multipartFile);
        }

        return "redirect:/";
    }
    @GetMapping(value = "/new")
    public String itemForm(Model model) {
        model.addAttribute("form", new ItemForm());
        return "item/createItemForm";
    }

    @PostMapping(value = "/new")
    public String create(ItemForm form, @RequestParam("image") MultipartFile file) throws Exception {
        Item item = new Item();
        item.setName(form.getName());
        item.setPrice(form.getPrice());
        item.setStockQuantity(form.getStockQuantity());
        item.setAuthor(form.getAuthor());
        item.setPublisher(form.getPublisher());
        item.setIsbn(form.getIsbn());
        item.setCreateDate(form.getCreateDate());
        item.setPages(form.getPages());
        item.setDescription(form.getDescription());
        String category1 = form.getCategory1();
        String category2 = form.getCategory2();

        if(categoryServiceImpl.findByCategoryName(category1, category2) == null){
            categoryServiceImpl.save(new Category(category1, category2));
        }

        Category findCategory = categoryServiceImpl.findByCategoryName(category1, category2);
        item.setCategory(findCategory);
        itemServiceImpl.saveItem(item, file);
        return "redirect:/item";
    }


    @GetMapping(value = "")
    public String listByKeyWord(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(required = false, defaultValue = "판매량") String sort_by,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        // 3자리 콤마를 위한 format
        DecimalFormat decFormat = new DecimalFormat("###,###");
        // 페이지 사이즈
        int size = 6;
        // 전체 상품 개수
        int allItemNum = itemServiceImpl.getAllItemNum();
        String variable;
        //
        if (sort_by.equals("최신")) {
            variable = "createDate";
        }else if(sort_by.equals("판매량")){
            variable = "total_purchase";
        }else{variable = "";}

        // 1페이지당 6개의 상품 호출
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(variable).descending());
        PageRequest pageRequest2 = PageRequest.of(page, size, Sort.by("createDate").descending());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인이 되어있는 경우
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String username = ((UserDetails) principal).getUsername();

            Member findMember = memberServiceImpl.findByUserId(username);

            // 장바구니에 상품을 담지 않았다면
            if (cartServiceImpl.findByMemberId(findMember.getId()) == null) {
                model.addAttribute("NOP", 0);
                model.addAttribute("totalPrice", 0);
            } else {
                Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
                List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());

                String totalPrice = decFormat.format(CartItem.getTotalPrice(findCartItems));
                model.addAttribute("NOP", findCartItems.size());
                model.addAttribute("cartItems", findCartItems);
                model.addAttribute("totalPrice", totalPrice);
            }
        }

        String country;
        int num = 0;
        // 0 : 전체, 1 : 국내, 2 : 외국
        if (type != null) {
            num = Integer.parseInt(type);
        }

        if (num == 0) country = "";
        else if(num == 1) country = "국내 도서";
        else country = "외국 도서";


        Page<Item> findAllItem = itemServiceImpl.findByKeyword(pageRequest, keyword, country);
        List<Item> bestseller = itemServiceImpl.findAllItem().subList(0,3);
        Long totalElement = findAllItem.getTotalElements();

        int pageNum = Math.floorDiv(allItemNum, size) - 1;
        int startPage = Math.max(page - 1, 0);
        int endPage = Math.min(page, pageNum);

        List<String> allCategory2 = categoryServiceImpl.findAllCategory2();


        // 국내 도서
        Page<Item> koreanList = itemServiceImpl.findByKeyword(pageRequest, "국내");
        // 외국 도서
        Page<Item> foreignList = itemServiceImpl.findByKeyword(pageRequest,"외국");

        long total_korean_item_num = koreanList.getTotalElements();
        long total_foreign_item_num = foreignList.getTotalElements();
        long total_item_num = findAllItem.getTotalElements();

            
        // 카테고리 목록
        Map<String, List<Integer>> categoryList = new HashMap<>();
        List<String> categories = categoryServiceImpl.findAllCategory2();

        for (int i=0; i<categories.size(); i++) {
            String category = categories.get(i);
            List<Item> byItemWithCategory = itemServiceImpl.findByItemWithCategory(category);
            List<Integer> temp = new ArrayList<>();
            temp.add(byItemWithCategory.size());
            temp.add(i);

            categoryList.put((category), temp);
        }


        model.addAttribute("keyword", keyword);
        model.addAttribute("country", country);

        model.addAttribute("paging", findAllItem);
        model.addAttribute("bestseller", bestseller);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("total_count", total_item_num);

        model.addAttribute("allCategory2", allCategory2);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", page);

        model.addAttribute("koreanNum", total_korean_item_num);
        model.addAttribute("foreignNum", total_foreign_item_num);
        model.addAttribute("categoryList", categoryList);
        return "item/itemList";
    }


    @GetMapping(value = "/{itemId}")
    public String showItem(@PathVariable("itemId") Long itemId,
                           @RequestParam(defaultValue = "0", required = false, value = "reviewPage") int reviewPage,
                           @RequestParam(defaultValue = "0", required = false) int inquiryPage,
                           @AuthenticationPrincipal Member member,
                           Model model) {
        DecimalFormat decFormat = new DecimalFormat("###,###");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String username = ((UserDetails) principal).getUsername();

            Member findMember = memberServiceImpl.findByUserId(username);

            if (cartServiceImpl.findByMemberId(findMember.getId()) == null) {
                model.addAttribute("NOP", 0);
                model.addAttribute("totalPrice", 0);
            } else {
                Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
                List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());

                String totalPrice = decFormat.format(CartItem.getTotalPrice(findCartItems));
                model.addAttribute("NOP", findCartItems.size());
                model.addAttribute("cartItems", findCartItems);
                model.addAttribute("totalPrice", totalPrice);
            }
        }

        int reviewSize = 10;
        int inquirySize = 10;

        Item item = itemServiceImpl.findOneItem(itemId);
        List<Item> sameCategoryItems = itemServiceImpl.findByItemWithCategory(item.getCategory().getCategory2());
        sameCategoryItems.remove(item);

        // 리뷰 처리
        // 해당 아이템에 해당하는 리뷰를 페이징 해서 가져옴
        Page<Review> findPageReviewByItemId = reviewServiceImpl.findPageReviewByItemId(PageRequest.of(reviewPage, reviewSize), itemId);
        List<Review> findAllReviewByItemId = reviewServiceImpl.findAllReviewByItemId(itemId);
        int reviewPageNum = findPageReviewByItemId.getTotalPages();
        int reviewStartPage = Math.max(reviewPage - 1, 0);
        int reviewEndPage = Math.min(reviewPage, reviewPageNum);

        // 카테고리 처리
        List<String> allCategory2 = categoryServiceImpl.findAllCategory2();
        // 점수별 리뷰 개수
        int[] reviewCountByScore = Review.countByScore(findAllReviewByItemId);
        // 평균 점수
        double avgScore = Review.calculateAvgScore(findAllReviewByItemId);

        // 문의 처리
        Page<Inquiry> allInquiry = inquiryServiceImpl.findByItemId(PageRequest.of(inquiryPage, inquirySize, Sort.by("created_at").descending()), itemId);

        model.addAttribute("reviewSize", reviewSize);
        model.addAttribute("reviewStartPage", reviewStartPage);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("reviewEndPage", reviewEndPage);
        model.addAttribute("inquirySize", inquirySize);

        model.addAttribute("allInquiry", allInquiry);

        model.addAttribute("allReview", findAllReviewByItemId);
        model.addAttribute("reviewCount", reviewCountByScore);
        model.addAttribute("pageReview", findPageReviewByItemId);
        model.addAttribute("allCategory2", allCategory2);


        model.addAttribute("avgScore", avgScore);

        model.addAttribute("item", item);
        model.addAttribute("groupItem", sameCategoryItems);
        return "item/itemDetail";
    }


    @GetMapping(value = "/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model
            model) {



        Item item = itemServiceImpl.findOneItem(itemId);
        ItemForm form = new ItemForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "item/updateItemForm";
    }


    @PostMapping(value = "/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form")
    ItemForm form) {
        itemServiceImpl.updateItem(itemId, form.getName(), form.getPrice(),
                form.getStockQuantity());
        return "redirect:/item";
    }

    @DeleteMapping(value = "/delete/{itemNum}")
    @Transactional
    @ResponseBody
    public void deleteItemById(@RequestParam String itemNum) {
        itemServiceImpl.deleteByItemId(Long.valueOf(itemNum));
    }


}
