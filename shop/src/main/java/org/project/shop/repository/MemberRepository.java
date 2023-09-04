package org.project.shop.repository;

import org.project.shop.domain.Member;

import java.util.List;

public interface MemberRepository {
    public void save(Member member);
    public Member findMember(Long id);
    public List<Member> findAllMember();
    public List<Member> findByEmail(String email);


}
