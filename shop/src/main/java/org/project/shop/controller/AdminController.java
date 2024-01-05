package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.*;
import org.project.shop.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
@Secured("ROLE_ADMIN")
public class AdminController {
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final InquiryServiceImpl inquiryServiceImpl;

    @GetMapping(value = "")
    public String adminPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<Member> memberList = memberServiceImpl.findAllGeneralMember();
        int size = Math.min(memberList.size(), 2);

        model.addAttribute("type", "member");
        model.addAttribute("memberList", memberList.subList(0, size));
        return "admin/adminHome";
    }

    @GetMapping(value = "/member")
    public String adminMemberPage(Model model) {
        List<Member> allGeneralMember = memberServiceImpl.findAllGeneralMember();
        model.addAttribute("allMember", allGeneralMember);
        model.addAttribute("type", "member");
        return "admin/adminHome";
    }

    @GetMapping(value = "/item")
    public String adminMemberItem(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        int pageSize = 6;
        int allItemNum = itemServiceImpl.getAllItemNum();
        int pageNum = Math.floorDiv(allItemNum, pageSize);
        int startPage = Math.max(page - 1, 0);
        int endPage = Math.min(page + 1, pageNum);

        Page<Item> allItem = itemServiceImpl.findByKeyword(PageRequest.of(page, 6), keyword);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("allItem", allItem);
        return "admin/adminProduct";
    }

    @PostMapping(value = "/item/{itemId}")
    @Transactional
    public void adminMemberItemEdit(HttpServletResponse response, @PathVariable String itemId, @RequestParam int size) throws IOException {
        Long id = Long.parseLong(itemId);
        Item findItem = itemServiceImpl.findOneItem(id);
        findItem.setStockQuantity(size);

        ScriptUtils.alertAndBackPage(response, "수정 되었습니다");
    }

    @PostMapping(value = "/item/status/{itemId}")
    @Transactional
    public void adminMemberItemStatusEdit(HttpServletResponse response, @PathVariable String itemId, @RequestParam Boolean status) throws IOException {
        Long id = Long.parseLong(itemId);
        Item findItem = itemServiceImpl.findOneItem(id);
        if (status) {
            findItem.setSale(true);
        } else {
            findItem.setSale(false);
        }

        ScriptUtils.alertAndBackPage(response, "수정 되었습니다");
    }

    @GetMapping(value = "/review")
    public String adminReview(Model model) {
        List<Review> allReview = reviewServiceImpl.findAllReview();

        model.addAttribute("allReview", allReview);
        return "/admin/adminReview";
    }

    @GetMapping(value = "/inquiry")
    public String adminInquiry(Model model) {
        List<Inquiry> allInquiry = inquiryServiceImpl.findAllInquiry();

        model.addAttribute("allInquiry", allInquiry);
        return "/admin/adminInquiry";
    }


    @GetMapping(value = "/order")
    public String adminMemberOrderList(Model model) {
        List<Order> allOrder = orderServiceImpl.findAllOrder();
        model.addAttribute("allOrder", allOrder);
        return "/admin/adminOrder";
    }

    @PostMapping(value = "/order/status/{orderId}")
    public String adminMemberOrderList(Model model, @PathVariable Long orderId) {
        Order findOrder = orderServiceImpl.findByOrderId(orderId);
        return "/admin/adminOrder";
    }
}
