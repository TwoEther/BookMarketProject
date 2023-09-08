package org.project.shop.repository;

import org.project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public void save(Member member);
    public Member findMember(Long id);
    public List<Member> findAllMember();
    public Optional<Member> findByEmail(String email);


}
