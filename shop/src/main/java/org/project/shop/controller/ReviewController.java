package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.*;
import org.project.shop.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/review")
public class ReviewController {
    private final MemberServiceImpl memberServiceImpl;
    private final OrderServiceImpl orderService;
    private final OrderItemServiceImpl orderItemServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    

    @GetMapping(value = "/manage")
    public String reviewPage() {
        return "review/written";
    }

    @GetMapping(value = "/written")
    public String writtenReviewPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        List<Order> findOrders = orderService.findOrderByMemberId(findMember.getId());

        List<Review> writtenReviews = reviewServiceImpl.findAllReviewByMemberId(findMember.getId());
        List<OrderItem> afterPaymentOrderItem = new ArrayList<>();
        for (Review review : writtenReviews) {
            Item item = review.getItem();
            for (Order findOrder : findOrders) {
                afterPaymentOrderItem.addAll(orderItemServiceImpl.findByOrderAndItemAfterPayment(findOrder.getId(), item.getId()));
            }

        }
        model.addAttribute("orderItems", afterPaymentOrderItem);
        return "review/written";
    }

    @GetMapping(value = "/notWritten")
    public String notWrittenReviewPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        List<Order> findOrders = orderService.findByMemberIdAfterPayment(findMember.getId());
        List<OrderItem> afterPaymentOrderItem = new ArrayList<>();

        for (Order order : findOrders) {
            afterPaymentOrderItem.addAll(orderItemServiceImpl.findByOrderAndItemAfterPayment(order.getId(), findMember.getId()));
        }

        List<Review> writtenReviews = reviewServiceImpl.findAllReviewByMemberId(findMember.getId());


        List<OrderItem> orderItemByOrderAndItem = new ArrayList<>();
        for (Review review : writtenReviews) {
            Item item = review.getItem();
            for (Order findOrder : findOrders) {
                orderItemByOrderAndItem.addAll(orderItemServiceImpl.findByOrderAndItemAfterPayment(findOrder.getId(), item.getId()));
            }

        }
        System.out.println("afterPaymentOrderItem = " + afterPaymentOrderItem);
        System.out.println("orderItemByOrderAndItem = " + orderItemByOrderAndItem);
        afterPaymentOrderItem.removeAll(orderItemByOrderAndItem);

        System.out.println("afterPaymentOrderItem = " + afterPaymentOrderItem);

        model.addAttribute("orderItems", afterPaymentOrderItem);
        return "review/notWritten";
    }
}
