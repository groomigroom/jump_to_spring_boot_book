package springboot.example.mysite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @GetMapping("/kimgroomi")
    @ResponseBody
    public String index() {
        return "안녕하세요. 김구름입니다.";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
        //즉시 /question/list url로 이동하라고 하는거
    }
}
