package org.project.shop.service;

import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.project.shop.repository.ItemRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemRepositoryImpl itemRepositoryImpl;

    public ItemServiceImpl(ItemRepositoryImpl itemRepositoryImpl) {
        this.itemRepositoryImpl = itemRepositoryImpl;
    }

    @Override
    @Transactional
    public void saveItemNoImage(Item item) {
        itemRepositoryImpl.save(item);
    }

    @Override
    @Transactional
    public void saveItem(Item item, MultipartFile file) throws Exception {
        // 프로젝트 경로
        String projPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();
        File uploadFile = new File(projPath, fileName);
        file.transferTo(uploadFile);
        // DB에 파일 넣기
        item.setFileName(fileName);
        item.setFilePath("/static/files/" + fileName);

        itemRepositoryImpl.save(item);
    }

    @Override
    public Item findOneItem(Long itemId) {
        return itemRepositoryImpl.findOneItem(itemId);
    }

    @Override
    public List<Item> findAllItem() {
        return itemRepositoryImpl.findAllItem();
    }

    @Override
    public Page<Item> findAllItem(PageRequest pageRequest) {
        return itemRepositoryImpl.findAllItem(pageRequest);
    }

    @Override
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepositoryImpl.findOneItem(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }

    @Override
    public int getAllItemNum() {
        return itemRepositoryImpl.getAllItemNum();
    }

    @Override
    public void orderItem(Long itemId, int quantity) {
        Item findItem = itemRepositoryImpl.findOneItem(itemId);
        int stockQuantity = findItem.getStockQuantity();
        findItem.setStockQuantity(stockQuantity-quantity);
    }

    @Override
    public boolean checkStockQuantity(Long itemId, int quantity) {
        return findOneItem(itemId).getStockQuantity() > quantity;
    }

    @Override
    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword) {
        if (keyword == null) {
            return itemRepositoryImpl.findAllItem(pageRequest);
        }
        return itemRepositoryImpl.findByKeyword(pageRequest, keyword);
    }

    @Override
    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword, String country) {
        if (keyword != null && country != null) {
            return itemRepositoryImpl.findByKeyword(pageRequest, keyword, country);
        }
        if (keyword == null && country != null) {
            return itemRepositoryImpl.findByCountry(pageRequest, country);
        }
        if (keyword != null) {
            return itemRepositoryImpl.findByKeyword(pageRequest, keyword);
        }
        return itemRepositoryImpl.findAllItem(pageRequest);

    }


    @Override
    public Page<Item> findByCountry(PageRequest pageRequest, String country) {
        return null;
    }


    @Override
    public List<Item> findByItemWithCategory(String category2) {
        return itemRepositoryImpl.findByItemWithCategory(category2);
    }

    @Override
    public List<Item> findBySortedTotalPurchase() {
        return itemRepositoryImpl.findBySortedTotalPurchase();
    }

    @Override
    public List<Item> orderByCategory() {
        return itemRepositoryImpl.orderByCategory();
    }

    @Override
    public void deleteByItemId(Long itemId) {
        itemRepositoryImpl.deleteByItemId(itemId);
    }


}
