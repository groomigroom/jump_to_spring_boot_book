package springboot.example.mysite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username);
    //자바 스프링 부트에서 Optional은 "값이 비어있을 수도 있고, 존재할 수도 있는 상태"를 나타내는 컨테이너 객체입니다.
}
