package org.project.shop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.shop.Repository.MemberRepository;
import org.project.shop.Service.MemberService;
import org.project.shop.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    public void join() throws Exception{
        //given
        Member member1 = new Member("kim");

        //when
        Long memberId = memberService.join(member1);

        //Then
        assertThat(memberId).isEqualTo(member1.getId());

    }

    @Test
    public void duplicateMemberCheck() throws Exception{
        Member member1 = new Member("lee");
        Member member2 = new Member("lee");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외가 발생");
    }

}
