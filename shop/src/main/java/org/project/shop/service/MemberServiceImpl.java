package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.repository.MemberRepositoryImpl;
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
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepositoryImpl memberRepositoryImpl;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberRepositoryImpl memberRepositoryImpl, PasswordEncoder passwordEncoder) {
        this.memberRepositoryImpl = memberRepositoryImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Long join(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepositoryImpl.save(member);
        return member.getId();
    }

    @Override
    public List<Member> findAllMember(){
        return memberRepositoryImpl.findAllMember();
    }

    @Override
    public Member findOneMember(Long memberId){
        return memberRepositoryImpl.findMember(memberId);
    }

    @Override
    public Member findByUserId(String userId) {
        return memberRepositoryImpl.findByUserId(userId);
    }

    @Override
    public void deleteMemberByMemberId(Long memberId) {
        memberRepositoryImpl.deleteMemberByMemberId(memberId);
    }

    @Override
    public String findMemberIdByEmailAndPhoneNum(String email, String phoneNum) {
        return memberRepositoryImpl.findMemberIdByEmailAndPhoneNum(email, phoneNum);
    }


    @Override
    public Member findById(Long id) {
        return memberRepositoryImpl.findById(id);
    }

    @Override
    public int checkDuplicateMember(String user_id) {
        if (user_id.isEmpty()) {
            return ExceptionCode.EMPTY.ordinal();
        } else if (!checkReqexId(user_id)) {
            return ExceptionCode.Reqex.ordinal();
        } else if (findByUserId(user_id) != null){
            return ExceptionCode.Dup.ordinal();
        } else {
            return ExceptionCode.OK.ordinal();
        }
    }

    @Override
    public boolean checkPassword(String id, String pw) {
        return passwordEncoder.matches(pw, memberRepositoryImpl.findByUserId(id).getPassword());
    }

    @Override
    public boolean checkReqexId(String id) {
        String pattern = "^[a-z0-9]{6,20}";
        return Pattern.matches(pattern, id);
    }

    @Override
    public boolean checkReqexPw(String pw) {
        String pattern = "\"^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,16}$\"";
        return Pattern.matches(pattern, pw);
    }
}
