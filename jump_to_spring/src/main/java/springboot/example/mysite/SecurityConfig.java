package springboot.example.mysite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
//@Configuration은 이 파일이 스프링부트의 환경 설정 파일임을 의미하는 애너테이션이다.
@EnableWebSecurity
//스프링 부트에서 @EnableWebSecurity는 웹 보안 기능(Spring Security)을 활성화하고, 개발자가 커스텀한 보안 설정(인증, 인가 등)을 애플리케이션에 적용하도록 알리는 애노테이션입니다. 필요한 스타터 의존성과 핵심 의미를 정리해 드립니다.
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    //@Bean은 스프링에 의해 생성 또는 관리되는 객체를 의미
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //SecurityFilterChain은 스프링 부트에서 웹 보안(인증 및 인가)을 처리하는 핵심 필터들의 집합을 의미합니다.
        //HttpSecurity는 스프링 시큐리티에서 웹 기반 보안 설정을 구성하는 핵심 빌더 클래스입니다.
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        //authorizeHttpRequests는 HTTP 요청들에 대한 URL별 접근 권한(인가, Authorization) 설정을 시작하겠다는 선언입니다.
                        // AntPathRequestMatcher 대신 일반 문자열로 경로를 지정합니다.
                        .requestMatchers("/**").permitAll())
                        //RequestMatchers는 Spring Security에서 "들어오는 HTTP 요청(URL, HTTP 메서드 등)이 내가 지정한 특정 조건과 일치하는지 검사하는 인터페이스"입니다.
                        //.permitAll()의미: "권한을 모두에게 허용한다"는 뜻입니다.
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/h2-console/**"))
                //.csrf (Cross-Site Request Forgery)개념: 사용자가 의도하지 않게 공격자가 지정한 행동(예: 비밀번호 변경, 결제 요청 등)을 신뢰하는 웹사이트에 요청하게 만드는 '사이트 간 요청 위조' 공격을 의미합니다.
                //.ignoringRequestMatchers()는 "내가 지정한 특정 URL 경로들에 대해서는 보안 검사(또는 특정 보안 기능)를 적용하지 않고 건너뛰겠다(Ignore)"는 의미를 가집니다.
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                //.headers(...)역할: HTTP 응답 헤더(Response Headers)와 관련된 보안 설정을 시작하는 메서드입니다.
                //addHeaderWriter(...)는 HTTP 응답 헤더에 개발자가 원하는 커스텀 헤더 작성기(HeaderWriter)를 직접 추가하는 메서드입니다.
                //XFrameOptionsHeaderWriter(...)는 HTTP 응답 헤더에 X-Frame-Options 문구를 자동으로 작성해 주는 클래스를 생성합니다.
                //XFrameOptionsMode를 X-Frame-Options 헤더의 값을 SAMEORIGIN으로 지정하는 열거형(Enum) 상수입니다. 
                //SAMEORIGIN은 "도메인이 같은(동일한 출처의) 페이지 내에서만 우리 웹페이지를 iframe으로 넣을 수 있다"는 뜻입니다. 예를 들어 내 사이트 주소가 example.com이라면, ://example.com 안에서는 ://example.com를 iframe으로 띄울 수 있지만, 해커의 사이트인 hacker.com에서는 내 사이트를 iframe으로 띄울 수 없게 차단합니다.
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                //.formLogin 메서드는 스프링 시큐리티의 로그인 설정을 담당하는 부분
                //로그인 페이지의 URL은 /user/login이고
                //로그인 성공 시에 이동할 페이지는 루트 URL(/)임을 의미한다
                .logout((logout) -> logout
                        .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
        //.build()는 "지금까지 채워 넣은 설정 데이터를 바탕으로, 최종적으로 사용할 실제 객체를 완성해서 만들어내라"는 명령입니다. 이는 자바 디자인 패턴 중 하나인 빌더 패턴(Builder Pattern)의 마지막 단계에 해당하는 메서드입니다.
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //BCryptPasswordEncoder는 해시 암호화하는거
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //AuthenticationManager는 Spring Security에서 사용자의 인증(Authentication) 처리를 총괄하는 가장 핵심적인 컨트롤러 인터페이스입니다.
        
        return authenticationConfiguration.getAuthenticationManager();
    }
}
