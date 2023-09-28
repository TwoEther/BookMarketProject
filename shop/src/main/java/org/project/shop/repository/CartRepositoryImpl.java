package org.project.shop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.Null;
import org.project.shop.domain.Cart;
import org.project.shop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartRepositoryImpl implements CartRepository{
    @PersistenceContext
    private EntityManager em;


    @Override
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
        return em.find(Cart.class, id);
    }

    @Override
    public Cart findByMemberId(Long memberId) {
        return em.createQuery("select c from Cart c where c.memberId = :memberId", Cart.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    @Override
    public void clear() {
        em.clear();
    }
}
