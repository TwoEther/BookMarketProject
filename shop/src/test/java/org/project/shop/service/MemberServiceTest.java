package org.project.shop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Member;
import org.project.shop.domain.Role;
import org.project.shop.repository.MemberRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        String id = "lee";
        String password = passwordEncoder.encode("password1");
        Member member1 = new Member(id, name);

        //when
        Long memberId = memberServiceImpl.join(member1);

        //Then
        Member findMember = memberServiceImpl.findByUserId(id);
        System.out.println("findMember.toString() = " + findMember.toString());
        System.out.println("memberRepository = " + memberRepository.findAllMember());

    }

    @Test
    @DisplayName("비밀번호 암호화 후 로그인 테스트")
    public void login() throws Exception {
        // 1. 기존에 회원 가입된 유저가 DB에 저장 되어 있어야 함
        // 2. 유저의 ID와 PW를 통해 유저를 찾을수 있어야 함

        //given
        String id = "lee";
        String name = "lee";
        String pw = "powkek";
        Member joinMember1 = new Member(id, name);


        // when
        memberServiceImpl.join(joinMember1);

        // then
        Member findMember = memberServiceImpl.findOneMember(joinMember1.getId());
        assertThat(findMember.getUserId()).isEqualTo(id);
        assertThat(findMember.getName()).isEqualTo(name);
        assertThat(findMember.getPassword()).isEqualTo(joinMember1.getPassword());

    }

    @Test
    public void duplicateMemberCheck() throws Exception{
        Member member1 = new Member("lee");
        Member member2 = new Member("lee");

        memberServiceImpl.join(member1);
        memberServiceImpl.join(member2);

        fail("예외가 발생");
    }

    @DisplayName("Member 권한 테스트")
    @Test
    public void memberRoleTest() {
        Member member1 = new Member("test1", "test1");
        Member member2 = new Member("test2", "test2");

        memberServiceImpl.join(member1);
        memberServiceImpl.join(member2);

        member1.setRole(Role.ROLE_ADMIN);
        member2.setRole(Role.ROLE_USER);

        assertThat(member1.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(member2.getRole()).isEqualTo(Role.ROLE_USER);

        System.out.println("member1.toString() = " + member1.toString());

    }
}
