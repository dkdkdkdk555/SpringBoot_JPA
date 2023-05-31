package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id // 키값
    @GeneratedValue // autoincrement
    @Column(name="member_id") // 칼럼명 매핑
    private Long id;

    @NotEmpty
    private String name;
    @Embedded // 임베디드(내장)타입
    private Address address;

    @JsonIgnore // 섹션2- 회원정보만 보고싶을때 주문정보는 안보여주기위해서, 섹션3- restapi 조회데이터 응답시 연관관계로 인해 무한루프 도는것을 방지하려고
    @OneToMany(mappedBy = "member") // 양방향 매핑 - 일대다, Order에 member필드에 매핑되버렸어
    private List<Order> orders = new ArrayList<>();
}
