package springboot.example.mysite;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
//자바 롬복(Lombok)의 @RequiredArgsConstructor는 클래스 내부에 final이나 @NonNull이 붙은 필드를 모아 생성자를 자동으로 만들어주는 어노테이션입니다. 
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final CommonUtil commonUtil;

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        //Model은 컨트롤러(Controller)에서 만든 데이터를 화면(View)으로 보내는 전달 매개체
        //스프링 부트(Spring Boot)에서 @PathVariable은 URL 경로(Path)에 포함된 동적인 값을 자바 메서드의 매개변수(파라미터)로 추출할 때 사용하는 어노테이션입니다. [1] RESTful API를 설계할 때 특정 리소스를 식별하기 위한 식별자(ID 등)를 URL에 자연스럽게 녹여내기 위해 자주 사용됩니다.
        Question question = this.questionService.getQuestion(id);

        String renderedMarkdown = this.commonUtil.markdown(question.getContent());

        model.addAttribute("question", question);
        //addAttribute("키", 값)의 의미이다.
        model.addAttribute("renderedContent", renderedMarkdown);
        return "qeustionDetail";
    }
    @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("isAuthenticated()")는 "이 메서드(또는 컨트롤러)는 로그인한(인증된) 사용자만 호출할 수 있게 하라"는 의미입니다. 
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "questionForm";
    }

    @PreAuthorize("isAuthenticated()")

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,  Principal principal) {
        if (bindingResult.hasErrors()) {
            return "questionForm";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        //http://localhost:8080/question/list?page=0 과 같은 방식으로 요청하기 위해서 int page에 @RequestParam이 사용됨
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "questionList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            //ResponseStatusException은 "개발자가 원하는 특정 HTTP 상태 코드(Status Code)와 에러 메시지를 클라이언트에게 가장 쉽고 빠르게 반환하기 위해 사용하는 예외 클래스"입니다. 
            //HttpStatus는 HTTP 요청에 대한 서버의 응답 상태를 나타내는 'HTTP 상태 코드'들을 모아둔 자바의 열거형(Enum) 클래스
            //BAD_REQUEST는 잘못된 요청의 의미 400
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "questionForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        //자바 스프링 부트에서 @Valid는 데이터 유효성 검증(Validation)을 자동으로 수행하라는 지시어입니다. 클래스 필드에 선언된 @NotNull, @Min, @Email 등의 제약 조건을 검증합니다.
        if (bindingResult.hasErrors()) {
            //BindingResult는 데이터 검증(Validation) 및 바인딩의 결과를 담는 인터페이스입니다. hasErrors()는 에러가 있는지 확인
            return "questionForm";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            //스프링 부트에서 ResponseStatusException은 "개발자가 원하는 특정 HTTP 상태 코드(Status Code)와 에러 메시지를 클라이언트에게 가장 쉽고 빠르게 반환하기 위해 사용하는 예외 클래스"입니다. 
            //HttpStatus는 HTTP 요청에 대한 서버의 응답 상태를 나타내는 'HTTP 상태 코드'들을 모아둔 자바의 열거형(Enum) 클래스
            //BAD_REQUEST는 잘못된 요청의 의미 400
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
