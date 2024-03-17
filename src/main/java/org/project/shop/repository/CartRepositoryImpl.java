package org.project.shop.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.Null;
import org.project.shop.domain.Cart;
import org.project.shop.domain.Member;
import org.project.shop.domain.QCart;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.project.shop.domain.QCart.cart;
import static org.project.shop.domain.QMember.member;

@Repository
public class CartRepositoryImpl implements CartRepository{
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public void save(Cart cart) {
        em.persist(cart);
    }

    @Override
    public Cart findById(Long id) {
        return queryFactory.select(cart)
                .from(cart)
                .where(cart.id.eq(id))
                .fetchOne();
    }
    @Override
    public Cart findByMemberId(Long memberId) {
        return queryFactory.select(cart)
                .from(cart)
                .leftJoin(cart.member, member)
                .fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public void deleteAll() {
        queryFactory.delete(cart)
                .execute();
    }

    @Override
    public void clear() {
        queryFactory.delete(cart).execute();
    }
}
