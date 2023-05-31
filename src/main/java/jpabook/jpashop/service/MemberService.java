package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor // 생성자 만들어줌
@RequiredArgsConstructor // final 있는 필드만 가지고 생성자 만들어줌
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired // 생성자 인잭션 --> 생성자가 하나만 있는경우에는 어노테이션 없어도 스프링이 autowired 인젝션으로 인식한다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional(readOnly = false) // 클래스가 readOnly true 여도 false 적용됨
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){ // 중복회원 검사
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 실무에선 name 을 unique constraint key 로 잡아라
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
//    @Transactional(readOnly = true) // 클래스에 true 있기 때문에 안해도됨
    public List<Member> findMember(){
        return memberRepository.findAll();
    }

    // 회원 단건 조회
//    @Transactional(readOnly = true) // 읽기전용이면 조회시 더 성능향상, 쓰기에는 true 설정하면안됨
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id); // db에서 가져오고 영속상태됨
        member.setName(name); // 영속성관리되는거를 수정해줌 --> 트랜잭션 끝나고 flush 될때 db수정
    }
}
