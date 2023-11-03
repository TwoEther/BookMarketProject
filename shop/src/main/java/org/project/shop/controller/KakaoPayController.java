package org.project.shop.controller;


import lombok.RequiredArgsConstructor;
import org.project.shop.domain.*;
import org.project.shop.kakaopay.KakaoApproveResponse;
import org.project.shop.service.*;
import org.project.shop.kakaopay.KakaoReadyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public KakaoReadyResponse payMethodPost(@RequestParam HashMap<String, Object> params) {
        String type = (String) params.get("type");
        String total_price = (String) params.get("total_price");
        String total_count = (String) params.get("total_count");
        String itemName = (String) params.get("itemName")+" 포함 "+total_count+"건";
        String index = (String) params.get("index");

        List<Integer> newSplitList = new ArrayList<>();
        List.of(index.split(",")).forEach(x -> {
            newSplitList.add(Integer.parseInt(x));
        });

        List<OrderItem> orderItems = new ArrayList<>();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());

        //OrderItem에 추가
        Order createdOrder = Order.createOrder(findMember);
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());
        createdOrder.setDelivery(delivery);

        orderServiceImpl.save(createdOrder);

        List<CartItem> paymentCartItems = new ArrayList<>();
        for (Integer i : newSplitList) {
            findCartItems.get(i).setIsPayment("O");
            paymentCartItems.add(findCartItems.get(i));
        }

        for (CartItem cartItem : paymentCartItems) {
            Item item = cartItem.getItem();
            int count = cartItem.getCount();

            OrderItem orderItem = OrderItem.createOrderItem(item, item != null ? item.getPrice() : 0, count);

            // 똑같은 주문
            List<OrderItem> allOrderItemByOrderAndItem = orderItemServiceImpl.findOrderItemByOrderAndItem(createdOrder.getId(), item.getId());
            if (allOrderItemByOrderAndItem.isEmpty()) {
                OrderItem createOrderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
                createOrderItem.setOrder(createdOrder);
                orderItems.add(orderItem);
                orderItemServiceImpl.save(createOrderItem);
            }
        }
        return kakaoPayService.kakaoPayReady(itemName, total_count, total_price);
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

        Order findOrder = orderServiceImpl.findByMemberIdBeforePayment(findMember.getId());
        findOrder.setStatus(OrderStatus.SUCCESS);
        findOrder.setTid(kakaoApprove.getTid());

        List<OrderItem> findOrderItems = orderItemServiceImpl.findOrderItemByOrderId(findOrder.getId());
        for (OrderItem findOrderItem : findOrderItems) {
            Item findItem = findOrderItem.getItem();
            CartItem findCartItem = cartItemServiceImpl.findByCartIdAndItemId(findCart.getId(), findItem.getId());
            if (findCartItem != null) {
                cartItemServiceImpl.deleteById(findCartItem.getId());
            }
        }


        model.addAttribute("kakaoApprove", kakaoApprove);
        model.addAttribute("orderItems", findOrderItems);
        return "order/payComplete";
    }
}
