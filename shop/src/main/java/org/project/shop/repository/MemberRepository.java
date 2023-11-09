package org.project.shop.repository;

import org.project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public void save(Member member);

    public void clear();
    public Member findMember(Long id);

    public List<Member> findAllGeneralMember();
    public List<Member> findAllMember();

    public void mergeMember(Member member);

    public Member findById(Long id);
    public Member findByName(String userName);

    public Member findByUserId(String userId);

    public String findMemberIdByEmailAndPhoneNum(String email, String phoneNum);
    public Member findById(String id);

    public void deleteMemberByMemberId(Long memberId);
}
