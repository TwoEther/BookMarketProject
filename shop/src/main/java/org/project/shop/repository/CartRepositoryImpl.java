package org.project.shop.repository;

<<<<<<< HEAD
import com.querydsl.jpa.JPAExpressions;
=======
>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a
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
        Member member = cart.getMember();
        if (member != null) {
            em.merge(member);
        }
        em.merge(cart);
        em.flush();
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
<<<<<<< HEAD
                .where(cart.member.eq(
                        JPAExpressions.selectFrom(member)
                                .where(member.id.eq(memberId))
                ))
=======
                .where(cart.member.id.eq(memberId))
>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a
                .fetchOne();
    }

    @Override
    public void clear() {
        queryFactory.delete(cart).execute();
    }
}
