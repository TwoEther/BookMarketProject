package org.project.shop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepositoryImpl implements CartRepository{
    @PersistenceContext
    private EntityManager em;


    @Override
    public void save(Cart cart) {
        em.persist(cart);
        em.flush();
    }

    @Override
    public Cart findById(Long id) {
        return em.find(Cart.class, id);
    }

    @Override
    public Cart findByName(String name) {
        return new Cart();
    }

    @Override
    public void clear() {
        em.clear();
    }
}
