package org.project.shop.repository;

import org.project.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository{
    public void save(Item item);

    public Item findOneItem(Long id);

    public List<Item> findAllItem();
}
