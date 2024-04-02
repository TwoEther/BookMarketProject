package org.project.shop.service;

import org.project.shop.auth.PrincipalDetails;
import org.project.shop.domain.Member;
import org.project.shop.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public PrincipalDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByUserId(username);
        if (findMember == null) {
            throw new UsernameNotFoundException("아이디를 찾을수 없습니다");
        }
        findMember.setUserId(findMember.getUserId());
        return new PrincipalDetails(findMember);
    }
}
