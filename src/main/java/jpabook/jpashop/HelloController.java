package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    @Transactional
    public String hello(Model model){
        model.addAttribute("data", "hello!!!"); // 뷰로 보내는 "data"라는 키값의 파라미터
        return "hello"; // hello.html 이 자동으로 붙음 -> Spring boot thymeleaf가 해준다.
    }
}
