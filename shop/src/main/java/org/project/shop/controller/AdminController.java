package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.domain.Role;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "")
    public String adminPage(Model model) {
        model.addAttribute("type", "member");
        return "admin/manage";
    }
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping(value = "")
//    public void denyToAdminPageGeneralUser(HttpServletResponse response) throws IOException {
//        ScriptUtils.alertAndBackPage(response, "접근권한이 없습니다.");
//    }

    @GetMapping(value = "/member")
    public String adminMemberPage(Model model) {
        List<Member> allGeneralMember = memberServiceImpl.findAllGeneralMember();
        model.addAttribute("allMember", allGeneralMember);
        model.addAttribute("type", "member");
        return "admin/manage";
    }

    @GetMapping(value = "/item")
    public String adminMemberItem(Model model) {
        PageRequest pageRequest = CustomPageRequest.customPageRequest();
        Page<Item> allItem = itemServiceImpl.findAllItem(pageRequest);
        model.addAttribute("allItem", allItem);
        model.addAttribute("type", "item");
        return "admin/manage";
    }
}
