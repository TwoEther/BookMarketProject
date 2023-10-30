package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.stream.Optional;
import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.service.CategoryServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final ItemServiceImpl itemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final MemberServiceImpl memberServiceImpl;


    @RequestMapping("/")
    public String home(Model model){
        List<String> categories = categoryServiceImpl.findAllCategory2();
        List<List<Item>> itemByCategory = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String username = ((UserDetails) principal).getUsername();

            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            Member findMember = memberServiceImpl.findByUserId(username);
            model.addAttribute("member", findMember);
        }



        for (String category : categories) {
            List<Item> findItem = itemServiceImpl.findByItemWithCategory(category);
            itemByCategory.add(findItem);
        }

        model.addAttribute("allItems", itemByCategory);
        return "home";
    }
}
