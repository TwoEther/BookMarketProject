package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.stream.Optional;
import org.project.shop.domain.*;
import org.project.shop.service.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final ItemServiceImpl itemServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final LikeItemServiceImpl likeItemServiceImpl;


    @RequestMapping("/")
    public String home(Model model){
        List<String> categories = categoryServiceImpl.findAllCategory2();
        List<List<Item>> itemByCategory = new ArrayList<>();
        DecimalFormat decFormat = new DecimalFormat("###,###");

        //로그인이 되어있는 경우
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String username = ((UserDetails) principal).getUsername();

            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            Member findMember = memberServiceImpl.findByUserId(username);

            if (cartServiceImpl.findByMemberId(findMember.getId()) == null) {
                model.addAttribute("totalPrice", 0);
                model.addAttribute("NOP", 0);
            }else{
                Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
                List<LikeItem> findAllLikeItem = likeItemServiceImpl.findLikeItemByMemberId(findMember.getId());
                List<CartItem> findCartItems = cartItemServiceImpl.findByCartId(findCart.getId());

                String totalPrice = decFormat.format(CartItem.getTotalPrice(findCartItems));
                model.addAttribute("NOP", findCartItems.size());

                model.addAttribute("cartItems", findCartItems);
                model.addAttribute("totalPrice", totalPrice);
                model.addAttribute("member", findMember);

            }
        }

        for (String category : categories) {
            List<Item> findItem = itemServiceImpl.findByItemWithCategory(category);
            itemByCategory.add(findItem);
        }


        // 베스트 셀러 처리

        List<Item> bySortedTotalPurchase = itemServiceImpl.findBySortedTotalPurchase();
        if (!bySortedTotalPurchase.isEmpty()) {
            List<Item> bestSeller = bySortedTotalPurchase.subList(0, 6);
            model.addAttribute("bestSeller", bestSeller);
        }

        model.addAttribute("allItems", itemByCategory);
        return "home";
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
