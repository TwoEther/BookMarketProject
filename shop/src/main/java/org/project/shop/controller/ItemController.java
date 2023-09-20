package org.project.shop.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Item;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.web.ItemForm;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;

    @GetMapping(value = "/item/new")
    public String itemForm(Model model) {
        model.addAttribute("form", new ItemForm());
        return "item/createItemForm";
    }

    @PostMapping(value = "/item/new")
    public String create(ItemForm form, @RequestParam("image") MultipartFile file) throws Exception {
        Item item = new Item();
        item.setName(form.getName());
        item.setStockQuantity(form.getStockQuantity());
        item.setIsbn(form.getIsbn());
        item.setAuthor(form.getAuthor());
        item.setPrice(form.getPrice());

        itemServiceImpl.saveItem(item, file);
        return "redirect:/item";
    }

    @GetMapping(value = "/item")
    public String list(Model model) {
        List<Item> items = itemServiceImpl.findItems();
        model.addAttribute("items", items);
        return "item/itemList";
    }

    @GetMapping(value = "/item/{itemId}")
    public String showItem(@PathVariable("itemId") Long itemId, Model
            model) {

        Item item = itemServiceImpl.findOneItem(itemId);
        model.addAttribute("item", item);
        return "item/itemDetail";
    }

    @GetMapping(value = "/item/{itemId}/edit")
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


    @PostMapping(value = "/item/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form")
    ItemForm form) {
        itemServiceImpl.updateItem(itemId, form.getName(), form.getPrice(),
                form.getStockQuantity());
        return "redirect:/item";
    }

}
