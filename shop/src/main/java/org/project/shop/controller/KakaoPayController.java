package org.project.shop.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.*;
import org.project.shop.kakaopay.KakaoApproveResponse;
import org.project.shop.service.*;
import org.project.shop.kakaopay.KakaoReadyResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
    private final OrderServiceImpl orderServiceImpl;
    private final KakaoPayService kakaoPayService;

    @PostMapping(value = "/ready")
    @ResponseBody
    @Transactional
    public KakaoReadyResponse beforePayRequest(@RequestParam HashMap<String, Object> params) {
        String type = (String) params.get("type");
        String total_price = (String) params.get("total_price");
        String total_count = (String) params.get("total_count");
        String itemName = (String) params.get("itemName")+" 포함 "+total_count+"건";

        return kakaoPayService.kakaoPayReady(itemName, total_count, total_price);
    }

    @GetMapping(value = "/payCancel")
    public void failPayRequest(HttpServletResponse response) throws IOException {
        ScriptUtils.alertAndBackPage(response, "결제가 취소 되었습니다");
    }

    @GetMapping("/paySuccess")
    public String afterPayRequest(@RequestParam("pg_token") String pgToken, Model model) {
        KakaoApproveResponse kakaoApprove = kakaoPayService.ApproveResponse(pgToken);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());
        List<OrderItem> paymentsItemList = new ArrayList<>();

        // 완료된 주문 건에 대해 주문을 저장
        Order paymentOrder = Order.createOrder(findMember);
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());

        paymentOrder.setDelivery(delivery);
        paymentOrder.setStatus(OrderStatus.SUCCESS);
        paymentOrder.setTid(kakaoApprove.getTid());
        orderServiceImpl.save(paymentOrder);

        // 장바구니에 있는 상품들을 OrderItem에 넣음
        for (CartItem cartItem : findCartItems) {
            Item item = cartItem.getItem();
            int count = cartItem.getCount();

            OrderItem orderItem = OrderItem.createOrderItem(item, item != null ? item.getPrice() : 0, count);
            orderItem.setOrder(paymentOrder);
            paymentsItemList.add(orderItem);
            orderItemServiceImpl.save(orderItem);

            cartItemServiceImpl.deleteById(cartItem.getId());
        }

        model.addAttribute("kakaoApprove", kakaoApprove);
        model.addAttribute("paymentsItemList", paymentsItemList);
        return "order/payComplete";
    }
}
