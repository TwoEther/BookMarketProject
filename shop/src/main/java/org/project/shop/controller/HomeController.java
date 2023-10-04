package org.project.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.project.shop.domain.Item;
import org.project.shop.service.ItemServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
public class HomeController {
    private final ItemServiceImpl itemServiceImpl;

    public HomeController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @RequestMapping("/")
    public String home(Model model){
        List<Item> findAllitem = itemServiceImpl.findItems();
        model.addAttribute("items", findAllitem);
        return "home";
    }
}
