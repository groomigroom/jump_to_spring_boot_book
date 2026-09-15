package springboot.example.mysite;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        //BindingResult는 데이터 검증(Validation) 및 바인딩의 결과를 담는 인터페이스입니다.
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            //"네가 보낸 이 객체 안에서, 내가 지정한 [특정 필드의 값]은 유효하지 않으니 검증 탈락(Reject)이야!" 라고 스프링 시스템에 공식적으로 등록하는 행위를 의미합니다.
            //bindingResult.rejectValue(필드명, 오류 코드, 오류 메시지)
            //bindingResult.rejectValue()는 컨트롤러로 넘어온 데이터(객체)의 특정 필드에 직접 검증 오류(ValidationError)를 등록할 때 사용하는 메서드입니다.
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            //DataIntegrityViolationException은 "데이터 무결성 제약 조건(Data Integrity Constraint)을 위반했다"는 것을 의미합니다.
            e.printStackTrace();
            //e.printStackTrace();는 "에러가 발생한 지점과 그 지점까지의 메서드 호출 흐름(Stack Trace)을 콘솔(표준 에러 스트림, System.err)에 그대로 출력하라"는 의미입니다.
            //이거 금기 코드이니 정리해 놓은거 참고하기
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            //bindingResult.reject()는 특정 필드를 지정하지 않고, 객체 전체의 유효성이 깨졌을 때 사용합니다.
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            //getMessage() 발생한 예외 내부에 저장된 구체적인 에러 원인 메시지를 반환합니다.
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
