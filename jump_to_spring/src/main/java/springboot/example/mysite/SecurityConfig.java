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
                        // AntPathRequestMatcher 대신 일반 문자열로 경로를 지정합니다.
                        .requestMatchers("/**").permitAll())
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/h2-console/**"))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //BCryptPasswordEncoder는 해시 암호화하는거
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
