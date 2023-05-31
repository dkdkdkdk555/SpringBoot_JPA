package jpabook.jpashop.domain.item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("A") // 테이블이 구분할 수 있도록
public class Album extends Item{

    private String artist;
    private String etc;
}
