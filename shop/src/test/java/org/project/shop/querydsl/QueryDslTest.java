package org.project.shop.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.shop.domain.Member;
import org.project.shop.domain.QMember;
import org.project.shop.repository.MemberRepositoryImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.project.shop.domain.QMember.member;

@SpringBootTest
@Transactional
public class QueryDslTest {
    @Autowired
    private MemberServiceImpl memberServiceImpl;
    @Autowired
    private MemberRepositoryImpl memberRepositoryImpl;

    @Autowired
    private JPAQueryFactory queryFactory;

    public List<Member> createMember(){
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String id = "id" + Integer.toString(i);
            String pw = "pw" + Integer.toString(i);
            String name = "name" + Integer.toString(i);
            members.add(new Member(id, pw, name));
        }
        return members;
    }

    @Test
    public void queryTest(){
        List<Member> members = createMember();

        String findId = "id3";
        for (Member member : members) {
            memberRepositoryImpl.save(member);
        }

        Optional<Member> findMember = Optional.ofNullable(queryFactory.select(member)
                .from(member)
                .where(member.userId.eq(findId))
                .fetchOne());
        System.out.println("findMember = " + findMember);
        assertThat(memberRepositoryImpl.findAllMember().size()).isEqualTo(10);
        assertThat(findMember.get().getName()).isEqualTo("name3");
    }
}
