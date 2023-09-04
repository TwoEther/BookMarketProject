package org.project.shop.service;

import org.junit.jupiter.api.Test;
import org.project.shop.domain.Member;
import org.project.shop.repository.MemberRepository;
import org.project.shop.repository.MemberRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    /*
        1. 회원가입을 성공해야함
        2. 같은 이름이 존재할 경우 예외를 발생시켜야 함
     */

    @Autowired
    private MemberRepositoryImpl memberRepository;
    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Member를 저장후 꺼내옴
    @Test
    public void join() throws Exception{
        //given
        String name = "lee";
        String email = "test@test.com";
        String password = passwordEncoder.encode("password1");
        Member member1 = new Member(email, name, password);

        //when
        Long memberId = memberServiceImpl.join(member1);

        //Then
        List<Member> findMember = memberServiceImpl.findByEmail(email);
        System.out.println("findMember.toString() = " + findMember.toString());
        System.out.println("memberRepository = " + memberRepository.findAllMember());
        assertThat(findMember.isEmpty()).isEqualTo(false);

    }

    @Test
    public void duplicateMemberCheck() throws Exception{
        Member member1 = new Member("lee");
        Member member2 = new Member("lee");

        memberServiceImpl.join(member1);
        memberServiceImpl.join(member2);

        fail("예외가 발생");
    }

}
