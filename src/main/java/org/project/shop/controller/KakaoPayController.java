package org.project.shop.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.auth.SnowflakeGenerator;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.*;
import org.project.shop.kakaopay.KakaoApproveResponse;
import org.project.shop.service.*;
import org.project.shop.kakaopay.KakaoReadyResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final SnowflakeGenerator snowflakeGenerator;


    @PostMapping(value = "/ready")
    @ResponseBody
    @Transactional
    public KakaoReadyResponse readyPayRequest(@RequestParam HashMap<String, Object> params) {
        String total_price = (String) params.get("total_price");
        String total_count = (String) params.get("total_count");
        String itemName = params.get("itemName") + " 포함 " + total_count + "건";

        return kakaoPayService.kakaoPayReady(itemName, total_count, total_price);
    }

    @PostMapping(value = "/payCancel")
    @Transactional
    @ResponseBody
    public KakaoReadyResponse readyCancelRequest(@RequestParam Long orderId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);

        Order findOrder = orderServiceImpl.findByMemberIdAfterPaymentOneOrder(orderId, findMember.getId());

        String tid = findOrder.getTid();
        int totalPrice = findOrder.getTotalPrice();

        List<OrderItem> orderItemByOrderId = orderItemServiceImpl.findOrderItemByOrderId(findOrder.getId());
        orderItemByOrderId
                .forEach(orderItem -> {
                    Item item = orderItem.getItem();
                    item.cancelTotalPurchase(orderItem.getCount());
                    orderItemServiceImpl.deleteOrderItem(orderItem.getId());
                });

        orderServiceImpl.deleteOrder(orderId);

        return kakaoPayService.kakaoPayCancelReady(tid, totalPrice);
    }


    @GetMapping("/paySuccess")
    @Transactional
    public String successPayRequest(@RequestParam("pg_token") String pgToken,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails,
                                    Model model) {
        if (principalDetails == null) {
            return "home";
        }

        String username = principalDetails.getUsername();

        KakaoApproveResponse kakaoApprove = kakaoPayService.ApproveResponse(pgToken);

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());
        List<OrderItem> paymentsItemList = new ArrayList<>();

        // 완료된 주문 건에 대해 주문을 저장
        Order paymentOrder = Order.createOrder(findMember);
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderNumber = String.valueOf(snowflakeGenerator.nextId());
        paymentOrder.setOrderNumber(nowTime + orderNumber);

        paymentOrder.setAddress(findMember.getAddress());
        paymentOrder.setStatus(OrderStatus.SUCCESS);

        // 주문번호
        paymentOrder.setTid(kakaoApprove.getTid());
        orderServiceImpl.save(paymentOrder);

        // 구매 총 가격을 구하는 로직
        int total_price = findCartItems.stream().mapToInt(cartItem -> {
            Item item = cartItem.getItem();
            int count = cartItem.getCount();

            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
            orderItem.setOrder(paymentOrder);
            orderItem.setDeliveryStatus(DeliveryStatus.READY);

            paymentsItemList.add(orderItem);
            orderItemServiceImpl.save(orderItem);
            cartItemServiceImpl.deleteById(cartItem.getId());
            return count * item.getPrice();
        }).sum();


        model.addAttribute("kakaoApprove", kakaoApprove);
        model.addAttribute("order", paymentOrder);
        model.addAttribute("total_price", total_price);
        model.addAttribute("member", findMember);
        model.addAttribute("paymentsItemList", paymentsItemList);
        return "order/payComplete";
    }
}


