package springboot.example.mysite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
//@ResponseStatus는 HTTP 요청이 성공하거나 실패했을 때 클라이언트에게 보낼 HTTP 응답 상태 코드(Status Code)를 직접 지정하는 어노테이션입니다.
//설정된 HTTP 상태 코드(HttpStatus.NOT_FOUND)와 이유("entity not found")
public class DataNotFoundException extends RuntimeException {
    //RuntimeException 컴파일러가 강제하지 않는 런타임 예외(Unchecked Exception)로 변환하여 다시 던질(throw) 때 사용하는 자바 코드입니다.
    private static final long serialVersionUID = 1L;
    public DataNotFoundException(String message) {
        super(message);
    }
}

/*
작성하신 코드에서 private static final long serialVersionUID = 1L;이 선언되는 이유는 자바의 직렬화(Serialization) 과정에서 클래스의 버전을 확인하고, 데이터가 안전하게 주고받아졌는지 검증하기 위한 '고유 식별자' 역할을 하기 때문입니다.
이를 이해하기 위해 꼭 알아야 할 핵심 개념을 쉽게 정리해 드릴게요.
------------------------------
## 💡 1. 직렬화(Serialization)가 무엇인가요?
자바에서 생성된 객체(메모리에 있는 데이터)를 파일로 저장하거나, 네트워크를 통해 다른 서버로 전송하려면 컴퓨터가 이해할 수 있는 바이트 형태로 변환해야 합니다. 이를 직렬화라고 합니다. (반대로 바이트 데이터를 다시 자바 객체로 바꾸는 것을 역직렬화라고 합니다.)

* 우리가 만드는 예외(Exception) 클래스의 부모인 RuntimeException과 그 상위인 Throwable은 자바 내부적으로 이 직렬화가 가능하도록 설계되어 있습니다(Serializable 인터페이스를 구현함).

## 🔍 2. serialVersionUID = 1L이 왜 필요할까요?
객체를 직렬화해서 어딘가로 보낸 후, 나중에 다시 객체로 변환(역직렬화)하려고 할 때 "내가 보냈던 그 클래스 구조가 지금 내 컴퓨터에 있는 클래스 구조와 백퍼센트 일치하는가?"를 확인해야 합니다.
이때 기준이 되는 버전 번호가 바로 serialVersionUID입니다.

   1. 버전 일치 확인: 직렬화할 때 1L이라는 번호표를 붙여서 보냅니다. 역직렬화할 때도 내 클래스에 1L이 적혀있으면 "동일한 클래스 버전이 맞구나!" 하고 안전하게 객체를 복원합니다.
   2. 에러 방지: 만약 이 번호가 없으면, 자바가 컴파일할 때 클래스의 필드나 메서드를 바탕으로 번호를 자동으로 계산해서 부여합니다. 하지만 클래스 코드가 아주 조금만 수정되어도 자동 생성되는 번호가 완전히 바뀌어 버립니다. 이 상태에서 과거에 저장해 둔 데이터를 읽으려고 하면 InvalidClassException이라는 에러가 발생하며 프로그램이 터지게 됩니다.

## 🛠️ 왜 하필 값 코드가 1L 인가요?

* 숫자 뒤의 L은 자바에서 이 값이 int가 아니라 long 타입 데이터임을 명시하는 기호입니다.
* 값 자체가 꼭 1일 필요는 없습니다. 2L, 12345L 등 아무 숫자나 상관없지만, "이 클래스의 첫 번째 버전이다"라는 의미로 관례상 1L을 가장 많이 사용합니다.

------------------------------
## 📌 한 줄 요약
DataNotFoundException 클래스의 코드가 나중에 수정되더라도, 기존에 직렬화되었던 예외 데이터들과 버전 불일치 에러 없이 안전하게 호환되도록 명시적인 버전 번호(1L)를 붙여준 것입니다.
------------------------------
인텔리제이(IntelliJ) 같은 개발 도구(IDE)를 사용하시다 보면 경고창에서 명시하라고 권장하여 자동으로 생성되는 경우가 많습니다. 혹시 지금 예외 처리 핸들러(@ControllerAdvice 등)를 통해 이 예외를 가로채는 작업을 하고 계신가요? 전체적인 예외 처리 구조를 잡는 과정에서 궁금한 점이 있다면 언제든 말씀해 주세요!


*/
