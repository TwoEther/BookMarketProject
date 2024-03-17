package org.project.shop.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.CartItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.project.shop.domain.QCart.cart;
import static org.project.shop.domain.QCartItem.cartItem;
import static org.project.shop.domain.QItem.item;

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
        return queryFactory.selectFrom(cartItem)
                .where(cartItem.cart.id.eq(cartId).
                        and(cartItem.item.id.eq(itemId)))
                .fetchOne();
    }

    @Override
    public CartItem findByCartItemId(Long id) {
        return queryFactory.select(cartItem)
                .from(cartItem)
                .where(cartItem.id.eq(id))
                .fetchOne();
    }


    @Override
    public void save(CartItem cartItem) {
        em.persist(cartItem);
    }


    @Override
    public void clear() {
        queryFactory.delete(cartItem).execute();
    }

    @Override
    public List<CartItem> findAllCartItem() {
        return queryFactory.selectFrom(cartItem).fetch();
    }


    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return queryFactory.selectFrom(cartItem)
                .leftJoin(cartItem.cart, cart)
                .fetchJoin()
                .where(cart.id.eq(cartId))
                .fetch();
    }

    @Override
    public List<CartItem> findCartItemByItem(Long itemId) {
        return queryFactory.selectFrom(cartItem)
                .where(cartItem.item.eq(
                        JPAExpressions.selectFrom(item)
                                .where(item.id.eq(itemId))
                ))
                .fetch();
    }

    @Override
    public List<Tuple> findItemIdByCartId(Long cartId) {
        return queryFactory.select(cartItem.item, cartItem.count)
                .from(cartItem)
                .where(cartItem.cart.id.eq(cartId))
                .fetch();
    }

    @Override
    public void deleteAll() {
        queryFactory.delete(cartItem)
                .execute();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        queryFactory.delete(cartItem)
                .where(cartItem.id.eq(id))
                .execute();
    }
}
