package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final OrderItemServiceImpl orderItemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    private final InquiryServiceImpl inquiryServiceImpl;

    @GetMapping(value = "/config")
    @Transactional
    public String config() {
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        String path = System.getProperty("user.dir")+"\\data.csv";
//        String path = "//home//ubuntu//shop//data.csv";
        String imagePath = System.getProperty("user.dir") + "\\src\\main\\resources\\images\\";

        String path1 = "C:\\lee\\Java\\data.csv";
        String path2 = "C:\\lee\\Project\\Spring\\data.csv";
        String imagePath1 = "C:\\lee\\Java\\bookImages\\";
        String imagePath2 = "C:\\lee\\Project\\Spring\\bookImages\\";

        System.out.println("path = " + path);
        try {
            br = Files.newBufferedReader(Paths.get(path));
            String line = "";

            while ((line = br.readLine()) != null) {
                List<String> tmpList = new ArrayList<String>();
                String newLine = line.replaceAll("@*", "@");
                String array[] = newLine.split("@");
                tmpList = Arrays.asList(array);
//                System.out.println(tmpList);
                ret.add(tmpList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        inquiryServiceImpl.deleteAll();
        reviewServiceImpl.deleteAll();
        itemServiceImpl.deleteAll();

        boolean isFirst = false;
        for (List<String> data : ret) {
            if(!isFirst) {
                isFirst = true;
                continue;
            }
            System.out.println("data = " + data);
            String title = data.get(0);
            int price = Integer.parseInt(data.get(1));
            int stockQuantity = Integer.parseInt(data.get(2));
            String author = data.get(3);
            String publisher = data.get(4);
            int isbn = Integer.parseInt(data.get(5));
            int page = Integer.parseInt(data.get(6));
            String description = data.get(7);
            String category1 = data.get(8);
            String category2 = data.get(9);

            System.out.println("title = " + title);
            System.out.println("category2 = " + category2);
            if (categoryServiceImpl.findByCategoryName(category1, category2) == null) {
                categoryServiceImpl.save(new Category(category1, category2));
            }
            Category findCategory = categoryServiceImpl.findByCategoryName(category1, category2);

            Item item = new Item(title, price, stockQuantity, author, publisher, isbn, page, description);
            item.setCategory(findCategory);

            // AWS 비용으로 인한 주석처리
            /*
            String fileRoot = imagePath + fileName+".png";
            File imageFile = new File(fileRoot);
            BufferedImage image = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos );
            baos.flush();
            MultipartFile multipartFile = new MockMultipartFile(fileName, baos.toByteArray());
             */
            itemServiceImpl.saveItemNoImage(item);
        }
        return "redirect:/";
    }
    @GetMapping(value = "/addItem")
    @Transactional
    public String addItemTest(Model model) {
        String path = System.getProperty("user.dir")+"\\data.csv";

        List<List<String>> books = new ArrayList<List<String>>();
        BufferedReader br = null;
        try{
            br = Files.newBufferedReader(Paths.get(path));
            int idx = 0;
            while((br.readLine()) != null){
                String sb = br.readLine();
                String[] tmp = sb.split("@");
                List<String> list = Arrays.stream(tmp).toList();
                idx += 1;
                // 10만개 데이터 제한
                if(idx >= 3000)break;
                books.add(list);
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
        cartItemServiceImpl.deleteAll();
        orderItemServiceImpl.deleteAllOrderItem();
        inquiryServiceImpl.deleteAll();
        reviewServiceImpl.deleteAll();
        itemServiceImpl.deleteAll();
        categoryServiceImpl.deleteAll();

        // 카테고리 초기화 로직
        List<String> category = new ArrayList<>();
        String[] category1 = {"국내 도서", "외국 도서"};
        String[] category2 = {"소설", "시/에세이","인문","가정/육아","요리","건강","취미/실용/스포츠",
                "경제/경영","자기계발","정치/사회","역사/문화","종교","예술/대중문화","과학","여행"};

        for (String cat1 : category1) {
            for (String cat2 : category2) {
                if(categoryServiceImpl.findByCategoryName(cat1, cat2)!= null) continue;
                categoryServiceImpl.save(new Category(cat1, cat2));
            }
        }
        List<Category> categories = categoryServiceImpl.findAllCategory();
        int idx = -1;
        for (List<String> data : books) {
            idx += 1;

            if(idx == 0) continue;
            // isbn 전처리
            int isbn = Integer.parseInt(data.get(1).substring(4));
            String name = data.get(3).split("-")[0];
            String author = data.get(4);
            String publisher = data.get(5);
            int price = Integer.parseInt(data.get(8).split("\\.")[0]);
            String imagePath = data.get(9);

            String description = data.get(10).substring(0, Math.min(data.get(10).length(), 100));

//
            Item item = new Item(name, price, 30, author, publisher, isbn, 250, description, imagePath);

            Category findCategory = categories.get(idx % categories.size());
            item.setCategory(findCategory);

            // AWS 비용으로 인한 주석처리
            /*
            String fileRoot = imagePath + fileName+".png";
            File imageFile = new File(fileRoot);
            BufferedImage image = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos );
            baos.flush();
            MultipartFile multipartFile = new MockMultipartFile(fileName, baos.toByteArray());
             */
            itemServiceImpl.saveItemNoImage(item);
        }
        return "redirect:/";
        
    }
    @PostMapping(value = "/dbConfig")
    @Transactional
    public String dbConfig(Model model,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        if (principalDetails == null) {
            return "redirect:/";
        }

        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);


        if (findMember.getRole().equals(Role.ROLE_USER.toString())) {
            return "redirect:/";
        }

        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
//        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\data.csv";
        String path = System.getProperty("user.dir")+"\\data.csv";
        String imagePath = System.getProperty("user.dir")+"\\src\\main\\resources\\images\\";

        String path1 = "C:\\lee\\Java\\data.csv";
        String path2 = "C:\\lee\\Project\\Spring\\data.csv";
        String imagePath1 = "C:\\lee\\Java\\bookImages\\";
        String imagePath2 = "C:\\lee\\Project\\Spring\\bookImages\\";


        try{
            br = Files.newBufferedReader(Paths.get(path));
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

        inquiryServiceImpl.deleteAll();
        reviewServiceImpl.deleteAll();
        itemServiceImpl.deleteAll();

        for (List<String> data : ret) {
            String title = data.get(0);
            int price = Integer.parseInt(data.get(1));
            int stockQuantity = Integer.parseInt(data.get(2));
            String author = data.get(3);
            String publisher = data.get(4);
            int isbn = Integer.parseInt(data.get(5));
            int page = Integer.parseInt(data.get(6));
            String description = data.get(7);
            String category1 = data.get(8);
            String category2 = data.get(9);

            if (categoryServiceImpl.findByCategoryName(category1, category2) == null) {
                categoryServiceImpl.save(new Category(category1, category2));
            }
            Category findCategory = categoryServiceImpl.findByCategoryName(category1, category2);

            Item item = new Item(title, price, stockQuantity, author, publisher, isbn, page, description);
            item.setCategory(findCategory);

            // AWS 비용으로 인한 주석처리
            /*
            String fileRoot = imagePath + fileName+".png";
            File imageFile = new File(fileRoot);
            BufferedImage image = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos );
            baos.flush();
            MultipartFile multipartFile = new MockMultipartFile(fileName, baos.toByteArray());
             */
            itemServiceImpl.saveItemNoImage(item);
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

        String path = System.getProperty("user.dir");

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
                           @RequestParam(defaultValue = "0", required = false, value = "inquiryPage") int inquiryPage,
                           @AuthenticationPrincipal PrincipalDetails principalDetails,
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

        // 문의 처리
        Page<Inquiry> findPageInquiryByItemId = inquiryServiceImpl.findByItemId(PageRequest.of(inquiryPage, inquirySize), itemId);
        List<Inquiry> findAllInquiryByItemId = inquiryServiceImpl.findAllInquiryByItemId(itemId);


        int reviewPageNum = findPageReviewByItemId.getTotalPages();
        reviewPage = Math.min(reviewPage, reviewPageNum);

        int reviewStartPage = Math.max(reviewPage - 1, 0);
        int reviewEndPage = Math.min(reviewPage, reviewPageNum);

        int inquiryPageNum = findPageInquiryByItemId.getTotalPages();
        inquiryPage = Math.min(inquiryPage, inquiryPageNum);

        int inquiryStartPage = Math.max(inquiryPage - 1, 0);
        int inquiryEndPage = Math.min(inquiryPage, inquiryPageNum);



        // 카테고리 처리
        List<String> allCategory2 = categoryServiceImpl.findAllCategory2();
        // 점수별 리뷰 개수
        int[] reviewCountByScore = Review.countByScore(findAllReviewByItemId);
        // 평균 점수
        double avgScore = Review.calculateAvgScore(findAllReviewByItemId);



        model.addAttribute("reviewSize", reviewSize);
        model.addAttribute("reviewStartPage", reviewStartPage);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("reviewEndPage", reviewEndPage);
        model.addAttribute("reviewCount", reviewCountByScore);
        model.addAttribute("allReview", findAllReviewByItemId);
        model.addAttribute("pageReview", findPageReviewByItemId);


        model.addAttribute("inquirySize", inquirySize);
        model.addAttribute("inquiryStartPage", inquiryStartPage);
        model.addAttribute("inquiryPage", inquiryPage);
        model.addAttribute("inquiryEndPage", inquiryEndPage);
        model.addAttribute("allInquiry", findAllInquiryByItemId);
        model.addAttribute("pageInquiry", findPageInquiryByItemId);



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
