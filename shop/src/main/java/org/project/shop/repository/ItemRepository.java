package org.project.shop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.project.shop.domain.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if(item.getId() == null){
            em.persist(item);
        }else{
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAllItem(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
