package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.repository.MemberRepository;
import org.project.shop.repository.MemberRepositoryImpl;
import org.project.shop.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepositoryImpl memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberRepositoryImpl memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Long join(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        System.out.println("member.toString() = " + member.toString());
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public List<Member> findAllMember(){
        return memberRepository.findAllMember();
    }

    @Override
    public Member findOneMember(Long memberId){
        return memberRepository.findMember(memberId);
    }

    @Override
    public Optional<Member> findById(String id) {
        return memberRepository.findById(id);
    }

    @Override
    public int checkDuplicateMember(String id) {
        if (id.isEmpty()) {
            return -1;
        } else if (!checkReqexId(id)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean checkReqexId(String id) {
        String pattern = "^[a-z0-9]{4,20}";
        return Pattern.matches(pattern, id);
    }


    // 반드시 구현해야하는 함수
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> loginMember = memberRepository.findById(username);
        if (loginMember.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다");
        }
        Member member = loginMember.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new User(member.getUserId(), member.getPassword(), authorities);
    }
}
