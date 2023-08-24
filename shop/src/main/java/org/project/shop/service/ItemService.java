package org.project.shop.service;

import org.project.shop.domain.Book;
import org.project.shop.domain.Item;
import org.project.shop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void saveItem(Book book) {
        itemRepository.save(book);
    }


    public Item findOneItem(Long itemId) {
        return itemRepository.findOneItem(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAllItem();
    }

    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepository.findOneItem(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }
}
