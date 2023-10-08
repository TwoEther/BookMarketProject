package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.repository.CartItemRepositoryImpl;
import org.project.shop.service.CartServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
    private final CartItemRepositoryImpl cartItemRepositoryImpl;


    @GetMapping(value = "/add")
    public String addCartGet(Model model) {
        return "redirect:/";
    }


    @PostMapping(value = "/add")
    @ResponseBody
    public boolean addCartItem(@RequestParam HashMap<String, Object> params) {

        long itemId = Long.parseLong((String) params.get("itemId"));
        int quantity = Integer.parseInt((String) params.get("quantity"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();

        Item findItem = itemServiceImpl.findOneItem(itemId);
        Cart cart = new Cart(findItem.getPrice());

        CartItem orderCartItem = new CartItem();
        orderCartItem.setCount(quantity);
        orderCartItem.setCart(cart);
        orderCartItem.setItem(findItem);


        if (itemServiceImpl.checkStockQuantity(itemId, quantity)) {
            Long id = cartServiceImpl.addCart(orderCartItem, username);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(value = "/list")
    public String cartList(Model model) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String username = ((UserDetails) principal).getUsername();
        } catch (Exception e) {
            return "redirect:/";
        }


        List<CartItem> cartItems = cartItemRepositoryImpl.findAllCartItem();
        model.addAttribute("cartItems", cartItems);
        return "cart/cartList";
    }

    //    @GetMapping(value = "/delete/{cartItemId}")
//    public String cartDeleteGet(@PathVariable Long cartItemId) {
//        System.out.println("cartItemId = " + cartItemId);
//        return "redirect:/";
//    }
    @DeleteMapping(value = "/delete/{cartItemId}")
    public String cartDelete(@PathVariable String cartItemId) {
        Long id = Long.parseLong(cartItemId);
        cartServiceImpl.deleteById(id);
        return "redirect:/";
    }

    @PutMapping(value = "/edit/{cartItemId}")
    @ResponseBody
    public String cartEdit(@PathVariable String cartItemId, @RequestParam String quantity) {
        Long id = Long.parseLong(cartItemId);
        System.out.println("id = " + id);
        CartItem findCartItem = cartItemRepositoryImpl.findByCartItemId(id);
        findCartItem.addCount(Integer.parseInt(String.valueOf(quantity)));
        return "redirect:/";
    }
}
