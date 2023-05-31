package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // junit실행시 스프링이랑 같이 엮어서 실행할게라는거
@SpringBootTest // 스프링컨테이너 안에서 테스트를 돌릴 수 있게 함
@Transactional // 트랜잭션 커밋 안되면 롤백시킴 --> 실제 서비스클래스에서는 이거있어도 롤백안시킴 그땐 오류낫을때 해당 트랜잭션 롤백시킴..
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository; // 같은 트랜잭션..

    @Test
    @Rollback(false) // @Transational이 커밋안한다고 롤백을 시키는데 그래도 insert문 보고싶으면 이거 설정해야함
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        /*
         insert 쿼리 안나감 ->  join() 들어가보면 em.persist()만 해놨음 persist가지고는 영속성관리만 하고 db인설트를 안하니까
         만약 테스트에서 엔티티매니져를 DI받고 해당 로직에서 em.flush()를 해주면 @Transactional 있어도 insert쿼리 날려진다..
         */

        //then
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class) // 해당예외인경우 return되서 나가짐
    public void 중복회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        //when
        memberService.join(member1);
//        try{
        memberService.join(member2); // 여기서 예외발생..!!
//        }catch (IllegalStateException e){
//            return; // 에러안뜸 return 되니까!
//        }

        //then
        fail("예외가 발생해야한다."); // 예외가 이전에 발생해야 하기 때문에 여기까지 오면 안된다!!!
    }
}