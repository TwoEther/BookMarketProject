package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.project.shop.domain.*;
import org.project.shop.domain.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.project.shop.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Order order) {
        em.persist(order);
    }

    @Override
    public Order findByOrderId(Long orderId) {
        return queryFactory.selectFrom(order)
                .where(order.id.eq(orderId))
                .fetchOne();
    }

    @Override
    public List<Order> findAllOrder() {
        return queryFactory.selectFrom(order)
                .fetch();
    }

    @Override
    public Order findByMemberIdBeforePayment(Long memberId) {
        return queryFactory.selectFrom(order)
                .where(order.member.id.eq(memberId).
                        and(order.status.eq(OrderStatus.READY))
                )
                .fetchOne();
    }

    @Override
    public List<Order> findByMemberIdAfterPayment(Long memberId) {
        return queryFactory.selectFrom(order)
                .where(order.member.id.eq(memberId).
                        and(order.status.eq(OrderStatus.SUCCESS))
                )
                .orderBy(order.orderDate.desc())
                .fetch();
    }

    @Override
    public Order findByMemberIdAfterPaymentOneOrder(Long orderId, Long memberId) {
        return queryFactory.selectFrom(order)
                .where(order.id.eq(orderId).and(
                        order.member.id.eq(memberId)
                ))
                .orderBy(order.orderDate.asc())
                .fetchOne();
    }

    @Override
    public List<Order> findOrderByMemberId(Long memberId) {
        return queryFactory.selectFrom(order)
                .where(order.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public void deleteOrder(Long orderId) {
        queryFactory.delete(order)
                .where(order.id.eq(orderId))
                .execute();
    }


    @Override
    public List<Order> findAllOrder(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }
}
