package org.project.shop.service;

import org.project.shop.repository.MemberRepositoryImpl;
import org.project.shop.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepositoryImpl memberRepository;
    public MemberServiceImpl(MemberRepositoryImpl memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByName(member.getName());
        if(!findMember.isEmpty()) throw new IllegalStateException("이미 존재하는 회원입니다");
    }

    @Override
    public List<Member> findAllMember(){
        return memberRepository.findAllMember();
    }

    @Override
    public Member findOneMember(Long memberId){
        return memberRepository.findMember(memberId);
    }

}
