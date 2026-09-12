package springboot.example.mysite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
//@ResponseStatus는 HTTP 요청이 성공하거나 실패했을 때 클라이언트에게 보낼 HTTP 응답 상태 코드(Status Code)를 직접 지정하는 어노테이션입니다.
//설정된 HTTP 상태 코드(HttpStatus.NOT_FOUND)와 이유("entity not found")
public class DataNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public DataNotFoundException(String message) {
        super(message);
    }
}
