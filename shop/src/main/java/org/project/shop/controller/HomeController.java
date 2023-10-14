package org.project.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.project.shop.service.CategoryServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {
    private final ItemServiceImpl itemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;

    public HomeController(ItemServiceImpl itemServiceImpl, CategoryServiceImpl categoryServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @RequestMapping("/")
    public String home(Model model){
        List<String> categories = categoryServiceImpl.findAllCategory2();
        List<List<Item>> itemByCategory = new ArrayList<>();

        for (String category : categories) {
            List<Item> findItem = itemServiceImpl.findByItemWithCategory(category);
            itemByCategory.add(findItem);
        }

        model.addAttribute("allItems", itemByCategory);
        return "home";
    }
}
