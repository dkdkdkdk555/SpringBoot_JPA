package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 기본패치전략이 eager -> Lazy 로 바꿔기
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // STRING이 enum값 추가시에도 장애안남
    private DeliveryStatus status;
}
