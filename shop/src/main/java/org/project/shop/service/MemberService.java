package org.project.shop.service;

import org.project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    public Long join(Member member);

    public List<Member> findAllMember();
    public Member findOneMember(Long memberId);

    public Member findByUserId(String userId);

    public void deleteMemberByMemberId(Long memberId);
    public String findMemberIdByEmailAndPhoneNum(String email, String phoneNum);

    public int checkDuplicateMember(String id);

    public boolean checkPassword(String id, String pw);
    public Member findById(Long id);
    public boolean checkReqexId(String id);

    public boolean checkReqexPw(String id);
}
