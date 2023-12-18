package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.Inquiry;
import org.project.shop.domain.QInquiry;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.project.shop.domain.QInquiry.inquiry;

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
    public List<Inquiry> findAllInquiry() {
        return queryFactory.selectFrom(inquiry)
                .fetch();
    }

    @Override
    public void delete(Long id) {
        queryFactory.delete(inquiry)
                .where(inquiry.id.eq(id))
                .execute();
    }
}
