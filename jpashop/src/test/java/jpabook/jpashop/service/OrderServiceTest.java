package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockExcepption;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);

        em.persist(book);

        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }


    @Test
    public void 상품주문() throws Exception {
        //given 이런게 주어졌을때
        Member member = createMember("member1");

        Item item = createBook("시골 JPA", 10000, 10);
        //when 이렇게 하면
        Long orderid = orderService.order(member.getId(), item.getId(), 2);

        //then 이렇게 된다.
        Order getOrder = orderRepository.findOne(orderid);

        Assert.assertEquals("상품 주문시 상태은 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종류 수가 정확해야함.", 1 , getOrder.getOrderitems().size());
        Assert.assertEquals("상품 주문시 상태은 ORDER", 20000, getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야함.", 8, item.getStockQuantity());
    }



    @Test(expected = NotEnoughStockExcepption.class)
    public void 상품주문_재고수량_초과() throws Exception {
        //given 이런게 주어졌을때
        Member member = createMember("member1");
        Item item = createBook("시골 JPA",10000,10);

        int orderCnt = 11;
        //when 이렇게 하면
        Long orderid = orderService.order(member.getId(), item.getId(), orderCnt);

        //then 이렇게 된다.
        fail("NotEnoughStockExcepption 발생해야 합니다.");
    }

    @Test
    public void 상품주문_취소() throws Exception {
        //given 이런게 주어졌을때
        Member member = createMember("member1");
        Item item = createBook("시골 JPA",10000,10);
        int orderCnt = 3;
        Long orderId = orderService.order(member.getId(),item.getId(),orderCnt);
        //when 이렇게 하면
        orderService.cancelOrder(orderId);

        //then 이렇게 된다.
        Order getOrder = orderRepository.findOne(orderId);
        Item getItem = itemRepository.findOne(item.getId());
        Assert.assertEquals("주문이 취소된 상태는 CANCEL이다.", OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("재고는 원복 .", 10, getItem.getStockQuantity() );
    }

}
