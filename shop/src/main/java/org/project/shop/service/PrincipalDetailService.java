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
<<<<<<< HEAD
        Member principal = memberRepository.findByUserId(username);
=======
        Member principal = memberRepository.findById(username);
>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a
        principal.setName(principal.getUserId());
        if (principal == null) {
            return null;
        } else {
            return new PrincipalDetails(principal);
        }
    }
}
