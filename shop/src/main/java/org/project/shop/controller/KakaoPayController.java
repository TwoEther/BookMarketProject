package org.project.shop.controller;


import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Member;
import org.project.shop.kakaopay.KakaoApproveResponse;
import org.project.shop.service.*;
import org.project.shop.kakaopay.KakaoReadyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/kakaopay")
@RequiredArgsConstructor
public class KakaoPayController {
    private final MemberServiceImpl memberServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final OrderItemServiceImpl orderItemServiceImpl;
    private final KakaoPayService kakaoPayService;

    @PostMapping(value = "/ready")
    @ResponseBody
    public KakaoReadyResponse payMethodPost(@RequestParam HashMap<String, Object> params) {
        String type = (String) params.get("type");
        String total_price = (String) params.get("total_price");
        String total_count = (String) params.get("total_count");
        String itemName = (String) params.get("itemName")+" 포함 "+total_count+"건";
        return kakaoPayService.kakaoPayReady(itemName, total_count, total_price);
    }

    @GetMapping("/paySuccess")
    public String afterPayRequest(@RequestParam("pg_token") String pgToken, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());



        KakaoApproveResponse kakaoApprove = kakaoPayService.ApproveResponse(pgToken);
        model.addAttribute("kakaoApprove", kakaoApprove.toString());
        return "order/payComplete";
    }
}
