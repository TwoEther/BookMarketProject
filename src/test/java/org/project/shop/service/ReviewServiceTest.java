package org.project.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.*;
import org.project.shop.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest

public class ReviewServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberServiceImpl memberServiceImpl;
    @Autowired
    private ItemServiceImpl itemServiceImpl;
    @Autowired
    private ReviewServiceImpl reviewServiceImpl;
    @Autowired
    OrderServiceImpl orderServiceImpl;
    @Autowired
    private OrderItemServiceImpl orderItemServiceImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @Transactional
    public void setUp() {
        Review review1 = new Review(4, "읽기 쉬워요");
        Review review2 = new Review(5, "정말 자세한 내용 입니다");
        Review review3 = new Review(2, "너무 어렵습니다");

        List<Review> reviews = new ArrayList<>(List.of(review1, review2, review3));
        reviews.forEach(review -> reviewServiceImpl.save(review));


        Member member1 = new Member("testId1", "password1", "name1", "0100101010", "test1@test.com");
        Member member2 = new Member("testId2", "password2", "name2", "0100101011", "test2@test.com");

        List<Member> members = new ArrayList<>(List.of(member1, member2));
        members.forEach(member -> memberServiceImpl.save(member));

        Item item1 = new Item("item1", 20000, 30);
        Item item2 = new Item("item2", 34000, 15);
        Item item3 = new Item("item3", 11000, 11);
        Item item4 = new Item("item4", 59000, 9);

        List<Item> items = new ArrayList<>(List.of(item1, item2, item3, item4));
        items.forEach(item -> itemServiceImpl.saveItemNoImage(item));

        Order order1 = Order.createOrder(member1);
        order1.setStatus(OrderStatus.SUCCESS);
        orderServiceImpl.save(order1);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getPrice(), 3);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getPrice(), 3);
        OrderItem orderItem3 = OrderItem.createOrderItem(item3, item3.getPrice(), 3);
        orderItem1.setOrder(order1);
        orderItem2.setOrder(order1);
        orderItem3.setOrder(order1);

        orderItemServiceImpl.save(orderItem1);
        orderItemServiceImpl.save(orderItem2);
        orderItemServiceImpl.save(orderItem3);

        review1.setMember(member1);
        review2.setMember(member1);
        review3.setMember(member2);

        review1.setItem(item1);
        review2.setItem(item1);
        review3.setItem(item2);

    }

    @AfterEach
    @Transactional
    public void after() {
        reviewServiceImpl.deleteAll();
        itemServiceImpl.deleteAll();
        orderItemServiceImpl.deleteAllOrderItem();
        memberServiceImpl.deleteAll();
        orderServiceImpl.deleteAllOrder();
    }


    @DisplayName("특정 아이템에 리뷰가 적용되는지 테스트")
    @Test
    public void reviewItemTest() {
        PageRequest pageRequest = CustomPageRequest.customPageRequest();
        List<Member> allMember = memberServiceImpl.findAllMember();
        List<Item> allItems = itemServiceImpl.findAllItem();
        List<Review> allReview = reviewServiceImpl.findAllReview();

        Member member1 = allMember.get(0);
        Member member2 = allMember.get(1);

        Item item1 = allItems.get(0);
        Item item2 = allItems.get(1);

        for (int i = 0; i < allReview.size() - 1; i++) {
            Review review = allReview.get(i);
            review.setMember(member1);
            review.setItem(item1);
        }
        allReview.get(allReview.size() - 1).setMember(member2);
        allReview.get(allReview.size() - 1).setItem(item2);


        // Item 에 해당 하는 리뷰 개수 테스트
        List<Review> allReviewByItemId1 = reviewServiceImpl.findAllReviewByItemId(item1.getId());
        List<Review> allReviewByItemId2 = reviewServiceImpl.findAllReviewByItemId(item2.getId());

        for (Review review : allReviewByItemId1) {
            System.out.println("review.toString() = " + review.toString());
        }

        assertThat(allReviewByItemId1.size()).isEqualTo(2);
        assertThat(allReviewByItemId2.size()).isEqualTo(1);

        // Member 에 해당 하는 리뷰 개수 테스트
        List<Review> allReviewByMemberId1 = reviewServiceImpl.findAllReviewByMemberId(allMember.get(0).getId());
        List<Review> allReviewByMemberId2 = reviewServiceImpl.findAllReviewByMemberId(allMember.get(1).getId());

        assertThat(allReviewByMemberId1.size()).isEqualTo(2);
        assertThat(allReviewByMemberId2.size()).isEqualTo(1);
    }

    @DisplayName("N+1 테스트")
    public void nPlusOneTest() {
        List<Review> allReview = reviewServiceImpl.findAllReview();
        assertThat(allReview.size()).isEqualTo(3);
    }
}
