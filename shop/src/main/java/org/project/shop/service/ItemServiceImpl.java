package org.project.shop.service;

import org.project.shop.domain.Book;
import org.project.shop.domain.Item;
import org.project.shop.repository.ItemRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemRepositoryImpl itemRepositoryImpl;

    public ItemServiceImpl(ItemRepositoryImpl itemRepositoryImpl) {
        this.itemRepositoryImpl = itemRepositoryImpl;
    }

    @Override
    @Transactional
    public void saveItem(Book book) {
        itemRepositoryImpl.save(book);
    }

    @Override
    public Item findOneItem(Long itemId) {
        return itemRepositoryImpl.findOneItem(itemId);
    }

    @Override
    public List<Item> findItems() {
        return itemRepositoryImpl.findAllItem();
    }

    @Override
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepositoryImpl.findOneItem(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }
}
