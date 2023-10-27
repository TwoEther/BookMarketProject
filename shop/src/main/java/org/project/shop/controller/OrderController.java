package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.*;
import org.project.shop.service.KakaoPayService;
import org.project.shop.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final KakaoPayService kakaoPayService;

    @GetMapping(value = "")
    public String createForm(Model model) {
        List<Member> members = memberServiceImpl.findAllMember();
        List<Item> items = itemServiceImpl.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping(value = "")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId, @RequestParam("count") int count) {
        orderServiceImpl.order(memberId, itemId, count);
        return "redirect:/orders";
    }


    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch
                                    orderSearch, Model model) {
        List<Order> orders = orderServiceImpl.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping(value = "/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderServiceImpl.cancelOrder(orderId);
        return "redirect:/orders";
    }

    @GetMapping(value = "/orderList")
    public String orderListByMember() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();
        return "order/orderList";
    }

    @GetMapping(value = "/payment")
    public String orderPaymentGet() {
        return "order/payment";
    }

    @PostMapping(value = "/payment")
    public String orderPayment(Model model) {
        /*
            view로 넘겨야 할 것들
            1. 구매자에 대한 정보(Member, Address)
            2. 구매 상품에 대한 정보(cartItem)
         */
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());

        int deliveryFee = Math.round(((float) CartItem.getTotalCount(findCartItems) / 5)) * 3500;
        int totalPrice = CartItem.getTotalPrice(findCartItems);
        int totalCount = CartItem.getTotalCount(findCartItems);
        int paymentPrice = deliveryFee + totalPrice;


        model.addAttribute("member", findMember);
        model.addAttribute("cartItems", findCartItems);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("paymentPrice", paymentPrice);
        model.addAttribute("totalPrice", totalPrice);

        return "order/payment";
    }
}