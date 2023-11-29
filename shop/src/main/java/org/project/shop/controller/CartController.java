package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.repository.CartItemRepositoryImpl;
import org.project.shop.service.CartItemServiceImpl;
import org.project.shop.service.CartServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
    private final CartServiceImpl cartServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping(value = "/add")
    public String addCartGet(Model model) {
        return "redirect:/";
    }


    @PostMapping(value = "/add")
    @ResponseBody
    @Transactional
    public boolean addCartItem(@RequestParam HashMap<String, Object> params){

        long itemId = Long.parseLong((String) params.get("itemId"));
        int quantity = Integer.parseInt((String) params.get("quantity"));


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        if (findMember != null) {
            Item findItem = itemServiceImpl.findOneItem(itemId);

            quantity = Math.min(quantity, findItem.getStockQuantity());
            Long id = cartServiceImpl.addCart(findMember, findItem, quantity);
            return true;
        } else {
            Long id = null;
            return false;
        }

    }

    @GetMapping(value = "/list")
    public String cartList(final Model model) {
        Object principal;
        principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        int price = 0;

        if (findCart != null) {
            List<CartItem> findCartItem = cartItemServiceImpl.findByCartId(findCart.getId());
            for (CartItem cartItem : findCartItem) price += cartItem.getItem().getPrice() * cartItem.getCount();
            model.addAttribute("cartItems", findCartItem);

        }else{
            List<CartItem> findCartItem = new ArrayList<>();
            model.addAttribute("cartItems", findCartItem);
        }



        model.addAttribute("msg", "장바구니");
        model.addAttribute("url", "home");
        model.addAttribute("price", price);

        return "cart/cartList";
    }

    @DeleteMapping(value = "/delete/{cartItemId}")
    public String cartDelete(@PathVariable String cartItemId) {
        Long id = Long.parseLong(cartItemId);
        cartItemServiceImpl.deleteById(id);
        return "redirect:/";
    }


    @PutMapping(value = "/edit")
    @Transactional
    @ResponseBody
    public void cartEdit(@RequestParam HashMap<String, String> params) {
        Long id = Long.parseLong(params.get("cartItemId"));
        int count = Integer.parseInt(params.get("count"));
        CartItem findCartItem = cartItemServiceImpl.findByCartItemId(id);
        Item findItem = itemServiceImpl.findOneItem(findCartItem.getItem().getId());

        findItem.setStockQuantity(findItem.getStockQuantity()+count);
        findCartItem.addCount(count);
    }
}
