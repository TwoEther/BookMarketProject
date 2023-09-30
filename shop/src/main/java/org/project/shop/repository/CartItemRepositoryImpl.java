package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.QCartItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.project.shop.domain.QCart.cart;
import static org.project.shop.domain.QCartItem.cartItem;

@Repository
public class CartItemRepositoryImpl implements CartItemRepository{
    @PersistenceContext
    EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CartItemRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
        return queryFactory.select(cartItem)
                .from(cartItem)
                .leftJoin(cartItem.cart, cart)
                .where(cart.id.eq(cartItem.cart.id))
                .fetchOne();
    }

    @Override
    public CartItem findById(Long id) {
        return queryFactory.select(cartItem)
                .from(cartItem)
                .where(cartItem.id.eq(id))
                .fetchOne();
    }

    @Override
    @Transactional
    public void save(CartItem cartItem) {
        em.persist(cartItem);
    }
}
