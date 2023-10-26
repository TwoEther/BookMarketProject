package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
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
    public boolean addCartItem(@RequestParam HashMap<String, Object> params) {

        long itemId = Long.parseLong((String) params.get("itemId"));
        int quantity = Integer.parseInt((String) params.get("quantity"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Item findItem = itemServiceImpl.findOneItem(itemId);

        if (itemServiceImpl.checkStockQuantity(itemId, quantity)) {
            Long id = cartServiceImpl.addCart(findMember, findItem, quantity);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(value = "/list")
    public String cartList(Model model) {
        Object principal;
        principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Member findMember = memberServiceImpl.findByUserId(username);
        Cart findCart = cartServiceImpl.findByMemberId(findMember.getId());
        List<CartItem> findCartItem = cartItemServiceImpl.findByCartId(findCart.getId());
        for (CartItem cartItem : findCartItem) {
            System.out.println("cartItem.toString() = " + cartItem.toString());
        }
        
        int price = 0;
        for (CartItem cartItem : findCartItem) price += cartItem.getItem().getPrice() * cartItem.getCount();

        model.addAttribute("cartItems", findCartItem);
        model.addAttribute("price", price);
        return "cart/cartList";
    }

    @DeleteMapping(value = "/delete/{cartItemId}")
    public String cartDelete(@PathVariable String cartItemId) {
        Long id = Long.parseLong(cartItemId);
        cartServiceImpl.deleteById(id);
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
