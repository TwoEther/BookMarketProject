package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.domain.Order;
import org.project.shop.domain.OrderSearch;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.project.shop.service.OrderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    @GetMapping(value = "/order")
    public String createForm(Model model) {
        List<Member> members = memberServiceImpl.findAllMember();
        List<Item> items = itemServiceImpl.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }
    @PostMapping(value = "/order")
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
}