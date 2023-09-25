package org.project.shop.repository;

import org.project.shop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository{
    public void save(Cart cart);

    public Cart findById(Long id);

    public Cart findByName(String name);

    public void clear();
}
