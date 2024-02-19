package org.project.shop.repository;

import org.project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    // 회원 가입
    public Long save(Member member);

    // 회원 조회
    public List<Member> findAllMember();
    public List<Member> findAllGeneralMember();
    public Member findOneMember(Long memberId);
    public Member findByUserId(String userId);
    public String findMemberIdByEmailAndPhoneNum(String email, String phoneNum);
    public Member findById(Long id);

    public Member findMember(Long id);

    public Optional<Member> findByEmail(String email);

    // 회원 삭제
    public void deleteMemberByMemberId(Long memberId);

    public void deleteAll();
}
