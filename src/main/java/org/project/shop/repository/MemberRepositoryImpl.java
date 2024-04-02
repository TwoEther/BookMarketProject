package org.project.shop.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.Member;
import org.project.shop.domain.QOrderItem;
import org.project.shop.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.project.shop.domain.QMember.member;
import static org.project.shop.domain.QOrder.order;
import static org.project.shop.domain.QOrderItem.orderItem;

@Repository
public class MemberRepositoryImpl implements MemberRepository{
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }


    @Override
    public Member findMember(Long id) {
        return queryFactory.select(member)
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<Member> findAllGeneralMember() {
        return queryFactory.selectFrom(member)
                .where(member.role.eq(Role.ROLE_USER.toString()))
                .fetch();
    }

    @Override
    public Member findOneMember(Long memberId) {
        return queryFactory.select(member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public List<Member> findAllMember(){
        return queryFactory.select(member)
                .from(member)
                .orderBy(member.created_at.asc())
                .fetch();
    }

    @Override
    public Member findById(Long id) {
        return queryFactory.select(member)
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(queryFactory.select(member)
                .where(member.email.eq(email))
                .fetchOne());
    }


    @Override
    public Member findByUserId(String userId) {
        return queryFactory.select(member)
                .from(member)
                .where(member.userId.eq(userId))
                .fetchOne();
    }

    @Override
    public String findMemberIdByEmailAndPhoneNum(String email, String phoneNum) {
        return queryFactory.select(member.userId)
                .from(member)
                .where(member.email.eq(email).and(
                        member.phoneNum.eq(phoneNum)
                )).fetchOne();

    }

    public Member findById(String userId) {
        return queryFactory.select(member)
                .from(member)
                .where(member.userId.eq(userId))
                .fetchOne();
    }

    @Override
    public void deleteMemberByMemberId(Long memberId) {
        queryFactory.delete(member)
                .where(member.id.eq(memberId))
                .execute();
    }

    @Override
    public void deleteAll() {
        queryFactory.delete(member)
                .execute();
    }

    @Override
    public List<Tuple> findAllMemberByOrderRank() {
        return queryFactory.select(member.nickName, orderItem.count.sum())
                .from(member)
                .innerJoin(member.orders, order)
                .innerJoin(order.orderItems, orderItem)
                .distinct()
                .groupBy(member.nickName)
                .orderBy(orderItem.count.sum().desc())
                .fetch();


    }
}
