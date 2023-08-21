package org.project.shop.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.shop.Repository.MemberRepository;
import org.project.shop.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanup(){
        memberRepository.deleteAll();
    }

    @Test
    void memberInsertTest() {
        //given
        Member newMember1 = new Member(1L, "kim");
        Member newMember2 = new Member(2L, "lee");
        
        memberRepository.save(newMember1);
        memberRepository.save(newMember2);

        //when
        List<Member> members = memberRepository.findAll();
        

        //then
        Assertions.assertThat(members.size()).isEqualTo(2);
        for (Member member : members) {
            System.out.println("member.toString() = " + member.toString());
        }
    }
}
