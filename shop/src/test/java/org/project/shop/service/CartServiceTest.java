package org.project.shop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Cart;
import org.project.shop.domain.Member;
import org.project.shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CartServiceTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @AfterEach
    public void dbClear() {
        cartRepository.clear();
    }

    @Test
    @DisplayName("DB에 들어가는지 테스트")
    public void saveTest() {
        Cart cart = new Cart(20000);
        cartService.save(cart);
        Cart findCart = cartService.findById(2L);

        assertThat(cart.getCartId()).isEqualTo(findCart.getCartId());
    }

    @Test
    @DisplayName("findByMemberId 테스트")
    public void findByMemberIdTest() {
        Member member = new Member();
        Cart cart = new Cart(member.getId(), 2000);
        System.out.println("cart.toString() = " + cart.toString());
    }
}