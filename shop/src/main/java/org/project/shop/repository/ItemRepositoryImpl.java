package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.project.shop.config.QuerydslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.project.shop.domain.Item;

import java.util.List;

import static org.project.shop.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;


    @Override
    public void save(Item item) {
        if(item.getId() == null){
            em.persist(item);
        }else{
            em.merge(item);
        }
    }

    @Override
    public Item findOneItem(Long id){
        return queryFactory.select(item)
                .from(item)
                .where(item.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<Item> findAllItem(){
        return queryFactory.select(item)
                .from(item)
                .fetch();
    }
}
