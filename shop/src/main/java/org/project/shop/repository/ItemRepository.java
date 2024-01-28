package org.project.shop.repository;

import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository {
    public void save(Item item);

    public void clear();
    public Item findOneItem(Long id);
    public List<Item> findAllItem();
    public Page<Item> findAllItem(PageRequest pageRequest);

    public List<Item> findByItemWithCategory(String category2);

    public int getAllItemNum();

    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword);
    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword, String country);

    public Page<Item> findByCountry(PageRequest pageRequest, String country);

//    public void deleteOneItem(Long id);

    public List<Item> orderByCategory();

    public void deleteByItemId(Long itemId);


    List<Item> findBySortedTotalPurchase();
}
