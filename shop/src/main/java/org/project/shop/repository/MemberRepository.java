package org.project.shop.repository;

import org.project.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public void save(Member member);

    public void clear();
    public Member findMember(Long id);
    public List<Member> findAllMember();

    public void mergeMember(Member member);
<<<<<<< HEAD

    public Member findById(Long id);
    public Member findByName(String userName);

    public Member findByUserId(String userId);
=======
    public Member findById(String id);
>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a


}
