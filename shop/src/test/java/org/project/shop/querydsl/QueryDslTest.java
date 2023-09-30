package org.project.shop.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.shop.domain.*;
import org.project.shop.repository.CartItemRepository;
import org.project.shop.repository.CartItemRepositoryImpl;
import org.project.shop.repository.ItemRepositoryImpl;
import org.project.shop.repository.MemberRepositoryImpl;
import org.project.shop.service.ItemServiceImpl;
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
    private ItemRepositoryImpl itemRepositoryImpl;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private CartItemRepositoryImpl cartItemRepositoryImpl;

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

    public List<Item> createItem(){
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String name = "name" + Integer.toString(i);
            int price = (int) (Math.random() * 30000) + 10000;
            int stockQuantity = (int) (Math.random() * 100);
            items.add(new Item(name, price, stockQuantity));
        }
        return items;
    }



    @DisplayName("특정 아이디를 가진 Member 조회")
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

    @DisplayName("모든 아이템 조회")
    @Test
    public void findAllItemTest() {
        List<Item> items = createItem();
        for (Item item : items) {
            itemRepositoryImpl.save(item);
        }
        List<Item> findAllItems = itemRepositoryImpl.findAllItem();

        assertThat(items.size()).isEqualTo(findAllItems.size());
    }




    @DisplayName("다중 조건 테스트")
    @Test
    public void findByCartIdAndItemIdTest(){
        Cart cart = new Cart();
        Item item = new Item();

        CartItem cartItem = CartItem.createCartItem(cart, item, 5);
        cartItemRepositoryImpl.save(cartItem);

        CartItem findCartItem = cartItemRepositoryImpl.findByCartIdAndItemId(cart.getId(), item.getId());
        assertThat(findCartItem.getCount()).isEqualTo(5);

    }
}
