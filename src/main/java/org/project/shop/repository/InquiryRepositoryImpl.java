package org.project.shop.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.Inquiry;
import org.project.shop.domain.QInquiry;
import org.project.shop.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.project.shop.domain.QInquiry.inquiry;
import static org.project.shop.domain.QItem.item;

@Repository
public class InquiryRepositoryImpl implements InquiryRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    public InquiryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(Inquiry inquiry) {
        em.persist(inquiry);
    }

    @Override
    public Inquiry findById(Long id) {
        return queryFactory.selectFrom(inquiry)
                .where(inquiry.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<Inquiry> findAllInquiryByItemId(Long itemId) {
        return queryFactory.selectFrom(inquiry)
                .where(inquiry.item.id.eq(itemId))
                .fetch();
    }

    @Override
    public Page<Inquiry> findByItemId(PageRequest pageRequest, Long id) {
        List<Inquiry> inquiries = queryFactory.selectFrom(inquiry)
                .where(inquiry.item.id.eq(id).and(
                        inquiry.member.role.eq(Role.ROLE_USER.toString())
                ))
                .orderBy(getOrderSpecifier(pageRequest))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(inquiry.count())
                .from(inquiry)
                .where(inquiry.id.eq(id));

        return PageableExecutionUtils.getPage(inquiries, pageRequest, countQuery::fetchOne);
    }

    @Override
    public List<Inquiry> findAllInquiryByGeneralMember() {
        return queryFactory.selectFrom(inquiry)
                .where(inquiry.member.role.eq(Role.ROLE_USER.toString()))
                .fetch();
    }

    @Override
    public void deleteAll() {
        queryFactory.delete(inquiry)
                .execute();
    }

    @Override
    public void delete(Long id) {
        queryFactory.delete(inquiry)
                .where(inquiry.id.eq(id))
                .execute();
    }

    public OrderSpecifier<?> getOrderSpecifier(PageRequest pageRequest) {
        // 정렬 조건이 있다면
        if (!pageRequest.getSort().isEmpty()) {
            for (Sort.Order order : pageRequest.getSort()) {
                Order direction = order.getDirection().isDescending() ? Order.DESC : Order.ASC;

                switch (order.getProperty()) {
                    case "created_at" -> {
                        return new OrderSpecifier(direction, inquiry.created_at);
                    }
                }
            }
        }

        // 정렬 조건이 없다면
        return new OrderSpecifier(Order.DESC, inquiry.created_at);
    }
}
