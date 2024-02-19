package org.project.shop.service;

import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    public void saveItemNoImage(Item item);
    public void saveItem(Item Item, MultipartFile file) throws Exception;
    public Item findOneItem(Long itemId);

    public List<Item> findAllItem();
    public Page<Item> findAllItem(PageRequest pageRequest);
    public void updateItem(Long itemId, String name, int price, int stockQuantity);

    public int getAllItemNum();


    public boolean checkStockQuantity(Long itemId, int quantity);

    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword);
    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword, String country);

    public Page<Item> findByCountry(PageRequest pageRequest, String country);
    
    public List<Item> findByItemWithCategory(String category2);

    public List<Item> findBySortedTotalPurchase();


    public void orderItem(Long itemId, int quantity);
    public List<Item> orderByCategory();

    public void deleteByItemId(Long itemId);

    public void deleteAll();
}
