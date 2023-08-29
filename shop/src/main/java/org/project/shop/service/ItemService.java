package org.project.shop.service;

import org.project.shop.domain.Book;
import org.project.shop.domain.Item;

import java.util.List;

public interface ItemService {
    public void saveItem(Book book);
    public Item findOneItem(Long itemId);
    public List<Item> findItems();
    public void updateItem(Long itemId, String name, int price, int stockQuantity);
}
