package org.project.shop.service;

import org.project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    public Long join(Member member);

    public List<Member> findAllMember();
    public Member findOneMember(Long memberId);

<<<<<<< HEAD
    public Member findById(Long id);
    public Member findByUserId(String userId);
=======
    public Member findById(String id);

>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a
    public int checkDuplicateMember(String id);

    public boolean checkPassword(String id, String pw);

    public boolean checkReqexId(String id);

    public boolean checkReqexPw(String id);
}
