package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;


@RestController // @Controller + @ResponseBody(데이터 자체를 json이나 xml로 응답하자) 과 같음
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMember();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> findMembers = memberService.findMember();
        List<MemberDto> collect = findMembers.stream()// 반복자리턴
                .map(m -> new MemberDto(m.getName(), m.getAddress())) // 람다식으로 m 이 반복자고 반복자의 내용물을 memberdto로 교체
                .collect(Collectors.toList()); // 컬렉션으로 다시 포장

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
        private Address address;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){ // @Valid = Member 필드에 명시된 유효성검사 조건대로 검증함..
        // @RequestBody => json을 Member객체로 바꿔줌
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")  // Put은 같은걸 여러번 수정,입력 해도 결과가 같다.. 그래서 수정에 적합
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{ // 보통 업데이트 조건이 더 까다로워서 dto를 따로 쓴다..
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberRequest{ // api 스펙과 매핑시킬 별도의 dto 객체

        @NotEmpty  // 널방지
        @NotBlank // 공백방지
        private String name;
    }

    @Data
    static class CreateMemberResponse{ // 등록된 아이디값 응답함

        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
}
