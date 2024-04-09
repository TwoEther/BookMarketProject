package org.project.shop.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.project.shop.domain.*;
import org.project.shop.domain.Order;
import org.project.shop.repository.CartItemRepositoryImpl;
import org.project.shop.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.couchbase.AutoConfigureDataCouchbase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.project.shop.domain.QCartItem.cartItem;
import static org.project.shop.domain.QMember.member;
import static org.project.shop.domain.QOrder.order;
import static org.project.shop.domain.QOrderItem.orderItem;

@ActiveProfiles("test")
@AutoConfigureDataCouchbase
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    MemberServiceImpl memberServiceImpl;
    @Autowired
    ItemServiceImpl itemServiceImpl;
    @Autowired
    OrderServiceImpl orderServiceImpl;
    @Autowired
    OrderRepositoryImpl orderRepositoryImpl;

    @Autowired
    OrderItemServiceImpl orderItemServiceImpl;

    @Autowired
    CartServiceImpl cartServiceImpl;

    @Autowired
    CartItemServiceImpl cartItemServiceImpl;
    @Autowired
    CartItemRepositoryImpl cartItemRepository;

    @BeforeEach
    public void setUp() {
        List<Member> memberList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();

        int memberSize = 500;
        int itemSize = 1000;

        for (int i = 1; i <= memberSize; i++) {
            Member member = Member.builder().
                    userId("id" + i).
                    password("pw" + i).
                    nickName("nickname" + i).
                    phoneNum("010-010-" + i).
                    email("email" + i).
                    build();
            memberList.add(member);

            em.persist(member);
        }

        for (int i = 1; i <= itemSize; i++) {
            Item item = Item.builder().
                    name("item" + i).
                    stockQuantity(1001).
                    price((int) (Math.random() * 10000)).build();
            itemList.add(item);
            itemServiceImpl.saveItemNoImage(item);
        }
        List<Member> allMember = memberServiceImpl.findAllMember();


        for (Member member : allMember) {
            Random random = new Random(System.currentTimeMillis());

            int n1 = random.nextInt(itemSize);
            int n2 = random.nextInt(itemSize);
            int n3 = random.nextInt(itemSize);

            Item item1 = itemList.get(n1);
            Item item2 = itemList.get(n2);
            Item item3 = itemList.get(n3);

            Order order = Order.createOrder(member);
            order.setTid(member.getPhoneNum());
            orderServiceImpl.save(order);

            OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getPrice(), 2);
            OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getPrice(), 2);
            OrderItem orderItem3 = OrderItem.createOrderItem(item3, item3.getPrice(), 2);

            orderItemServiceImpl.save(orderItem1);
            orderItemServiceImpl.save(orderItem2);
            orderItemServiceImpl.save(orderItem3);

            if (n1 % 2 == 0) {
                Order extendOrder = Order.createOrder(member);
                extendOrder.setTid(member.getPhoneNum() + "extend");
                orderServiceImpl.save(extendOrder);

                OrderItem orderItem4 = OrderItem.createOrderItem(item1, item1.getPrice(), 2);
                OrderItem orderItem5 = OrderItem.createOrderItem(item2, item2.getPrice(), 2);
                OrderItem orderItem6 = OrderItem.createOrderItem(item3, item3.getPrice(), 2);

                orderItemServiceImpl.save(orderItem4);
                orderItemServiceImpl.save(orderItem5);
                orderItemServiceImpl.save(orderItem6);

                orderItem4.setOrder(extendOrder);
                orderItem5.setOrder(extendOrder);
                orderItem6.setOrder(extendOrder);
            }

            orderItem1.setOrder(order);
            orderItem2.setOrder(order);
            orderItem3.setOrder(order);

        }
    }

    @AfterEach
    public void cleanUp() {
        orderItemServiceImpl.deleteAllOrderItem();
        orderServiceImpl.deleteAllOrder();
        itemServiceImpl.deleteAll();
        memberServiceImpl.deleteAll();
    }


    @DisplayName("구매 테스트")
    @Test
    public void cartItemToOrderItemTest() {
        int memberSize = 500;
        List<Member> allMember = memberServiceImpl.findAllMember();
        assertThat(allMember.size()).isEqualTo(memberSize);

    }
    @DisplayName("fetchJoin 성능테스트")
    @Test
    public void fetchJoinPerformTest() {
        Member member3 = memberServiceImpl.findByUserId("id3");
        System.out.println("========================");
        long start1 = System.currentTimeMillis();
        List<Order> ordersByFetchJoin = queryFactory.select(order)
                .from(order)
                .leftJoin(order.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
        System.out.println("========================");

        long diff1 = (System.currentTimeMillis() - start1);
        System.out.println("diff_fetch(s) = " + diff1);
    }

    @DisplayName("No_fetchJoin 성능테스트")
    @Test
    public void fetchNoJoinPerformTest() {
        long start2 = System.currentTimeMillis();
        System.out.println("========================");
        List<Order> ordersNoFetchJoin = queryFactory.select(order)
                .from(order)
                .leftJoin(order.member, member)
                .fetch();
        long diff2 = (System.currentTimeMillis() - start2);
        System.out.println("========================");
        System.out.println("diff_no_fetch(s) = " + diff2);
    }

}
