package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @GetMapping(value = "")
    public String adminPage(Model model) {
        model.addAttribute("type", "member");
        return "admin/manage";
    }

    @GetMapping(value = "/member")
    public String adminMemberPage(Model model) {
        List<Member> allGeneralMember = memberServiceImpl.findAllGeneralMember();
        model.addAttribute("allMember", allGeneralMember);
        model.addAttribute("type", "member");
        return "admin/manage";
    }

    @GetMapping(value = "/item")
    public String adminMemberItem(Model model) {
        List<Item> allItem = itemServiceImpl.findItems();
        model.addAttribute("allItem", allItem);
        model.addAttribute("type", "item");
        return "admin/manage";
    }
}
