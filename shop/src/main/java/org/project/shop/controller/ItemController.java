package org.project.shop.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.project.shop.domain.Category;
import org.project.shop.domain.CategoryItem;
import org.project.shop.domain.Item;
import org.project.shop.service.CategoryItemServiceImpl;
import org.project.shop.service.CategoryService;
import org.project.shop.service.CategoryServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.web.ItemForm;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.data.util.TypeUtils.type;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final CategoryItemServiceImpl categoryItemServiceImpl;

    @GetMapping(value = "/dbConfig")
    public String dbConfig(Model model) throws Exception {
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;

        try{
            br = Files.newBufferedReader(Paths.get("C:\\lee\\Java\\\\BookMarketProject\\data.csv"));
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

            String fileRoot = "C:\\lee\\Java\\\\BookMarketProject\\bookImages\\" + fileName+".png";
            Item item = new Item(title, price, stockQuantity, author, publisher, isbn, page, description);
            System.out.println("fileRoot = " + fileRoot);
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

<<<<<<< HEAD
        System.out.println("form.getCategory1() = " + form.getCategory1());
        System.out.println("form.getCategory2() = " + form.getCategory2());
=======
        String category1 = form.getCategory1();
        String category2 = form.getCategory2();

        if (categoryServiceImpl.findByCategoryName(category1, category2) == null) {
            Category findCategory = new Category(category1, category2);
            categoryServiceImpl.save(findCategory);
        }
        Category findCategory = categoryServiceImpl.findByCategoryName(category1, category2);

        CategoryItem categoryItem = new CategoryItem();
        categoryItem.setItem(item);
        categoryItem.setCategory(findCategory);

        categoryItemServiceImpl.save(categoryItem);
>>>>>>> 16b61c9b69a8d380269626d10c49fc8f9a30ba77

        itemServiceImpl.saveItem(item, file);
        return "redirect:/item";
    }

    @GetMapping(value = "")
    public String list(Model model) {
        List<Item> items = itemServiceImpl.findItems();
        model.addAttribute("items", items);
        return "item/itemList";
    }

    @GetMapping(value = "/{itemId}")
    public String showItem(@PathVariable("itemId") Long itemId, Model
            model) {

        Item item = itemServiceImpl.findOneItem(itemId);
        model.addAttribute("item", item);
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

}
