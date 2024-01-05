package org.project.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.*;
import org.project.shop.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/review")
public class ReviewController {
    private final MemberServiceImpl memberServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final OrderItemServiceImpl orderItemServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    private final LikeReviewServiceImpl likeReviewServiceImpl;
    

    @GetMapping(value = "/manage")
    public String reviewPage() {
        return "review/written";
    }

    @PostMapping(value = "/add")
    @Transactional
    public void addReview(HttpServletRequest httpServletRequest, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        /*
            1단계 : form 으로 부터 리뷰 내용과 별점을 받아서 저장 (성공)
            2단계 : 로그인 한 유저만 리뷰를 작성 하게끔 변경 (성공)
            3단계 : 해당 물건을 구매한 유저면 구매자, 아니면 비 구매자 표시 (성공)
                - 해당 맴버가 주문한 아이템 중에서 찾아야됨
            ★ 중요 : Null 값 처리
         */

        if (principalDetails == null) {
            ScriptUtils.alertAndBackPage(response,"로그인한 유저만 리뷰를 작성할 수 있습니다");
        }
        String username = principalDetails.getUsername();

        String review_detail = httpServletRequest.getParameter("review");
        String fscore = httpServletRequest.getParameter("rating");
        String fitemId = httpServletRequest.getParameter("itemId");

        String msg = " 입력되지 않았습니다";
        if(review_detail == null ){ScriptUtils.alertAndBackPage(response, "리뷰가"+msg);}
        if(fscore == null ){ScriptUtils.alertAndBackPage(response, "별점이"+msg);}

        int score = Integer.parseInt(fscore);
        Long itemId = Long.parseLong(fitemId);

        Review review = new Review(score, review_detail);
        Member findMember = memberServiceImpl.findByUserId(username);

        Item findItem = itemServiceImpl.findOneItem(itemId);
        review.setItem(findItem);
        review.setMember(findMember);

        List<Order> orderByMember = orderServiceImpl.findOrderByMemberId(findMember.getId());
        List<Item> paymentItems = new ArrayList<>();
        for (Order order : orderByMember) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                paymentItems.add(orderItem.getItem());
            }
        }

        if (paymentItems.contains(findItem)){review.setType("구매자");}
        else {review.setType("비구매자");}

        reviewServiceImpl.save(review);
        ScriptUtils.alertAndBackPage(response, "리뷰가 작성 되었습니다");

    }

    @DeleteMapping(value = "/delete/{reviewId}")
    @ResponseBody
    @Transactional
    public boolean deleteReview(HttpServletResponse response, @RequestParam String reviewId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if (principalDetails == null) {
            return false;
        }else{
            Long id = Long.valueOf(reviewId);
            String username = principalDetails.getUsername();
            Member findMember = memberServiceImpl.findByUserId(username);
            Review findReview = reviewServiceImpl.findOneReview(id);

            if (!findReview.getMember().getUserId().equals(username)) {
                return false;
            }else{
                reviewServiceImpl.deleteReview(id);
                return true;
            }
        }
    }

    @PostMapping(value = "/like/{reviewId}")
    @ResponseBody
    @Transactional
    public int addLikeReview(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam String reviewId) {
        Long id = Long.valueOf(reviewId);
        Review findReview = reviewServiceImpl.findOneReview(id);

        // 로그인이 안된 경우
        if (principalDetails == null) {
            return 0;
        }

        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);
        LikeReview findLikeReview = likeReviewServiceImpl.findByReviewIdAndMemberId(findReview.getId(), findMember.getId());
        System.out.println("findLikeReview = " + findLikeReview);

        // 좋아요를 누르지 않았다면
        if (findLikeReview == null) {
            LikeReview likeReview = new LikeReview();
            likeReview.setMember(findMember);
            likeReview.setReview(findReview);
            likeReviewServiceImpl.save(likeReview);

            findReview.addLike();
            return 1;

        } else{
            System.out.println("findLikeReview.toString() = " + findLikeReview.toString());
            findLikeReview.setStatus(false);
            findReview.cancelLike();
            likeReviewServiceImpl.deleteLikeReview(findLikeReview.getId());
            return 2;
        }
    }

    @GetMapping(value = "/written")
    public String writtenReviewPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        List<Order> findOrders = orderServiceImpl.findOrderByMemberId(findMember.getId());

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
        List<Order> findOrders = orderServiceImpl.findByMemberIdAfterPayment(findMember.getId());
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
