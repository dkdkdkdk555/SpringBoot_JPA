package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders") // 테이블 이름 매핑
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 서비스에서 생성자로 생성 막음
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 양방향 매핑 - 다대일 (FK 가지고 있으므로 얘가 연관관계주인)
    @JoinColumn(name="member_id") // FK 매핑
    private Member member;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Order에 컬렉션으로 들어와 있는것에 cascade설정을하면 order persist시 컬랙션도 persist가 됨
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
        persist(orderItemA)
        persist(orderItemB)
        persist(orderItemC)
        persist(order)

        -----케스케이드 적용-----=>

        persist(order)

     */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id") // 일대일이지만 fk가 있는곳에 연관관계주인을 잡는다.
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [Order, Cancle]

    // 연관관계 편의 메소드 --> 세터 대체
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메소드==// --> 생성자로 생성하지 말고 메소드로 생성하자
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ // ... => 가변인자, ...붙은 인자는 파라미터로 전달 안되거나 여러개 전달 돼도 됌
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==/
    /**
        주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
