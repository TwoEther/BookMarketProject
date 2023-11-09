package org.project.shop.service;

import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.project.shop.domain.QItem.item;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public CartServiceImpl(CartRepository cartRepository, MemberRepository memberRepository, ItemRepository itemRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Long addCart(Member findMember, Item findItem, int quantity) {
        /*
            1. 장바구니에 담을 상품을 조회한다
            2. 현재 로그인한 회원을 조회한다
            3. 현재 로그인한 회원의 장바구니 엔티디를 조회한다
                3.1 상품을 처음으로 장바구니에 담을경우 엔티티 생성
            4. 현재 상품이 장바구니에 들어있는지 조회
                4.1 이미 있는 상품의 경우 기존 수량에 추가
            5. 장바구니를 생성후 저장
         */

        if(cartRepository.findByMemberId(findMember.getId()) == null) {
            Cart newCart = Cart.createCart(findMember);
            cartRepository.save(newCart);
        }
        Cart findCart = cartRepository.findByMemberId(findMember.getId());

        CartItem savedItem = cartItemRepository.findByCartIdAndItemId(findCart.getId(), findItem.getId());

        findItem.removeStock(quantity);
        // 이미 장바구니에 존재하는 아이템이라면?
        if (savedItem != null) {
            savedItem.addCount(quantity);
            return savedItem.getId();
        }else{
            CartItem findCartItem = CartItem.createCartItem(findCart, findItem, quantity);
            cartItemRepository.save(findCartItem);
            return findCartItem.getId();
        }

    }

    @Override
    public Cart findByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }
}
