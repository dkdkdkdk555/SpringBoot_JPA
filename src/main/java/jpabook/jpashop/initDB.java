package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.model.domain.internal.MapMember;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * * userA
 * * * JPA1 BOOK
 * * * JPA2 BOOK
 * * userB
 * * * SPRING1 BOOK
 * * * SPRING2 BOOK
 */
@Component // 컴포넌트 스캔 대상됨
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct // 얘부터 실행
    public void init(){
        initService.dbInit();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit(){
            Member member =createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book);

            Book book2 = createBook("JPA2 BOOK", 10000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);


        }

        public void dbInit2(){
            Member member =createMember("userB", "당진", "2", "2222");
            em.persist(member);

            Book book = createBook("SPRING1 BOOK", 10000, 100);
            em.persist(book);

            Book book2 = createBook("SPRING2 BOOK", 10000, 100);
            em.persist(book);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);


        }

        private static Book createBook(String bookName, int bookPrice, int bookQuantity) {
            Book book = new Book();
            book.setName(bookName);
            book.setPrice(bookPrice);
            book.setStockQuantity(bookQuantity);
            return book;
        }

        private static Member createMember(String name, String a, String s, String s2) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(a, s, s2));
            return member;
        }
    }
}
