package org.project.shop.service;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.project.shop.domain.Member;
import org.project.shop.domain.Role;
import org.project.shop.repository.MemberRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class MemberServiceTest {
    /*
        1. 회원가입을 성공해야함
        2. 같은 이름이 존재할 경우 예외를 발생시켜야 함
     */

    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        Member member1 = new Member("id1", "password1", "name1", "010-1921-2392", "email1@email.com");
        Member member2 = new Member("id2", "password2", "name2", "010-1921-2393", "email2@email.com");

        member1.setRole(Role.ROLE_ADMIN.toString());
        member2.setRole(Role.ROLE_USER.toString());

        memberServiceImpl.save(member1);
        memberServiceImpl.save(member2);
    }

    @AfterEach
    public void cleanUp() {
        memberServiceImpl.deleteAll();
    }
    // Member를 저장후 꺼내옴
    @Test
    @DisplayName("회원가입 테스트")
    public void join() throws Exception{
        //Then
        Member findMember = memberServiceImpl.findByUserId("id1");
        assertThat(findMember.getUserId()).isEqualTo("id1");
    }

    @DisplayName("Member 권한 테스트")
    @Test
    public void memberRoleTest() {
        Member member1 = memberServiceImpl.findByUserId("id1");
        Member member2 = memberServiceImpl.findByUserId("id2");


        assertThat(member1.getRole()).isEqualTo(Role.ROLE_ADMIN.toString());
        assertThat(member2.getRole()).isEqualTo(Role.ROLE_USER.toString());
    }
}
