package springboot.example.mysite;

import lombok.Getter;

@Getter
public enum UserRole {
    //Enum은 상수(변하지 않는 값)들의 집합을 정의할 때 사용합니다. 권한의 종류는 제한적이고 명확하므로(관리자, 회원 등) 텍스트나 숫자 대신 Enum을 사용하면 오타로 인한 버그를 원천 차단할 수 있습니다.
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    //ADMIN: 관리자 권한을 대표하는 이름입니다. 실제 매핑된 텍스트는 "ROLE_ADMIN"입니다.
    //USER: 일반 사용자 권한을 대표하는 이름입니다. 실제 매핑된 텍스트는 "ROLE_USER"입니다.

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
