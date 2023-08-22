package org.project.shop.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
    void setUp(){
        memberRepository.deleteAll();
    }

    @Test
    void memberFindTest() {
        //given
        Member newMember1 = new Member("kim");
        Member newMember2 = new Member("lee");
        
        memberRepository.save(newMember1);
        memberRepository.save(newMember2);

        //when
        List<Member> members = memberRepository.findAll();
        Optional<Member> findMember = memberRepository.findById(1L);

        //then
        System.out.println("--------- findAllMember ---------");
        assertThat(members.size()).isEqualTo(2);
        for (Member member : members) {
            System.out.println("member.toString() = " + member.toString());
        }
        System.out.println("--------- findAllMember ---------\n");

        System.out.println("--------- findMember ---------");
        assertThat(findMember.get().getName()).isEqualTo(newMember1.getName());
        System.out.println(findMember.toString());
        System.out.println("--------- findMember ---------");
    }

    @Test
    void memberDeleteTest() {
        //given
        Member newMember1 = new Member("kim");
        Member newMember2 = new Member("lee");

        memberRepository.save(newMember1);
        memberRepository.save(newMember2);

        //when
        memberRepository.deleteById(3L);
        List<Member> allMember = memberRepository.findAll();
        for (Member member : allMember) {
            System.out.println("member = " + member);
        }
        //then
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
        assertThat(memberRepository.findById(3L)).isEmpty();
    }


}
