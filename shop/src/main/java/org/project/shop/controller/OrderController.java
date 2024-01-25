package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.*;
import org.project.shop.service.KakaoPayService;
import org.project.shop.service.*;
import org.project.shop.web.AddressForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
    private final OrderItemServiceImpl orderItemServiceImpl;
    private final KakaoPayService kakaoPayService;

    @GetMapping(value = "")
    public String createForm(Model model) {
        PageRequest pageRequest = CustomPageRequest.customPageRequest();

        List<Member> members = memberServiceImpl.findAllMember();
        Page<Item> items = itemServiceImpl.findAllItem(pageRequest);
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
    public String orderListByMember(@AuthenticationPrincipal PrincipalDetails principalDetails ,
                                    HttpServletResponse response, Model model) throws IOException {
        if (principalDetails == null) {
//            ScriptUtils.alert(response, "로그인 후 이용 가능 합니다.");
            return "redirect:/";
        }
        String username = principalDetails.getUsername();
        if (username.isEmpty()) {
//            ScriptUtils.alert(response, "로그인 후 이용 가능 합니다.");
            return "redirect:/";
        }


        Member findMember = memberServiceImpl.findByUserId(username);
        List<Order> findAllOrder = orderServiceImpl.findByMemberIdAfterPayment(findMember.getId());
        if (findAllOrder.isEmpty()) {
            model.addAttribute("allOrder", findAllOrder);
        } else {
            List<List<Item>> paymentList = new ArrayList<>();
                for (Order order : findAllOrder) {
                List<OrderItem> allOrder = orderItemServiceImpl.findOrderItemByOrderId(order.getId());
                List<Item> findAllItem = OrderItem.findAllItem(allOrder);
                paymentList.add(findAllItem);
            }

            model.addAttribute("allOrder", findAllOrder);
        }


        return "order/orderList";
    }

    @GetMapping(value = "/address")
    public String setDeliveryOrder(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                    Model model) throws IOException {
        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);
        Address address = findMember.getAddress();

        String zipcode = (address == null) ? "우편 번호" : address.getZipcode();
        String address1 = (address == null) ? "주소" : address.getAddress1();
        String address2 =  (address == null) ? "상세 주소" :address.getAddress2();
        String reference = (address == null) ? "참고 항목" :address.getReference();


        model.addAttribute("zipcode", zipcode);
        model.addAttribute("address1", address1);
        model.addAttribute("address2", address2);
        model.addAttribute("reference", reference);

        model.addAttribute("AddressForm", new AddressForm());

        return "member/address";
    }

    @PostMapping(value = "/address")
    @Transactional
    public void setDeliveryOrderPost(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletResponse response, AddressForm addressForm) throws IOException {
        String zipcode = addressForm.getZipcode();
        String address1 = addressForm.getAddress1();
        String address2 = addressForm.getAddress2();
        String reference = addressForm.getReference();

        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);
        Address address = new Address(zipcode, address1, address2, reference);
        findMember.setAddress(address);
        ScriptUtils.alertAndBackPage(response,"배송지가 설정 되었습니다");
    }

    @GetMapping(value = "/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model) {
        Order findOrder = orderServiceImpl.findByOrderId(orderId);
        model.addAttribute("order", findOrder);

        return "order/orderDetail";
    }
    // 구매 로직
    @GetMapping(value = "/payment")
    @Transactional
    public String orderPayment(Model model, HttpServletResponse response) throws IOException{
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

        // 장바구니가 비어있는 상태에서 구매를 시도한 경우
        if (findCart == null) {
            ScriptUtils.alertAndBackPage(response, "장바구니가 비었습니다");
        }

        List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());
        System.out.println("findCartItems = " + findCartItems);

        if (findCartItems.isEmpty()) {
            return "redirect:/";
        } else {
            List<Integer> newSplitList = new ArrayList<>();
            List<CartItem> paymentCartItems = findCartItems;


            int deliveryFee = Math.round(((float) CartItem.getTotalCount(findCartItems) / 5)) * 3500;
            int totalPrice = CartItem.getTotalPrice(paymentCartItems);
            int totalCount = CartItem.getTotalCount(paymentCartItems);
            int paymentPrice = deliveryFee + totalPrice;

            
            model.addAttribute("index", "te,st");
            model.addAttribute("member", findMember);
            model.addAttribute("cartItems", paymentCartItems);
            model.addAttribute("deliveryFee", deliveryFee);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("paymentPrice", paymentPrice);
            model.addAttribute("totalPrice", totalPrice);

            model.addAttribute("AddressForm", new AddressForm());
            return "order/payment";

        }



    }
}