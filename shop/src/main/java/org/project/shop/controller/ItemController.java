package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.project.shop.service.CategoryServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.web.ItemForm;
import org.project.shop.web.SearchForm;
import org.springframework.mock.web.MockMultipartFile;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping(value = "/dbConfig")
    public String dbConfig(Model model) throws Exception {
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        String path1 = "C:\\lee\\Java\\data.csv";
        String path2 = "C:\\lee\\Project\\Spring\\data.csv";
        String imagePath1 = "C:\\lee\\Java\\bookImages\\";
        String imagePath2 = "C:\\lee\\Project\\Spring\\bookImages\\";

        try{
            br = Files.newBufferedReader(Paths.get(path2));
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

            String fileRoot = imagePath2 + fileName+".png";
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
    public String list(Model model, SearchForm form) {
        String keyword = form.getKeyword();
        List<Item> findAllItem = itemServiceImpl.findByKeyword(keyword);

        model.addAttribute("items", findAllItem);
        model.addAttribute("keyword", keyword);
        return "item/itemList";
    }

    @GetMapping(value = "/{itemId}")
    public String showItem(@PathVariable("itemId") Long itemId, Model
            model) {

        Item item = itemServiceImpl.findOneItem(itemId);
        List<Item> sameCategoryItems = itemServiceImpl.findByItemWithCategory(item.getCategory().getCategory2());
        sameCategoryItems.remove(item);

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
