package org.project.shop.repository;

import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository{
    public void save(Item item);

    public void clear();
    public Item findOneItem(Long id);

    public List<Item> findAllItem();

    public List<Item> findByItemWithCategory(String category2);

    public List<Item> findByKeyword(String keyword);
//    public void deleteOneItem(Long id);

    public List<Item> orderByCategory();

}
