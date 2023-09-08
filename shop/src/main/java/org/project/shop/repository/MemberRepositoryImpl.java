package org.project.shop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
        em.flush();
    }

    @Override
    public Member findMember(Long id) {
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAllMember(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        Optional<Member> member = Optional.empty();
        try {
             member = Optional.ofNullable(em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            member = Optional.empty();
        }finally {
        }
        System.out.println("member.toString() = " + member.toString());
        return member;

    }

}
