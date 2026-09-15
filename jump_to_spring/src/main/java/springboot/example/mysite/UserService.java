package springboot.example.mysite;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        //PasswordEncoder는 사용자의 비밀번호를 안전하게 단방향 암호화(해시)하고, 입력된 비밀번호가 저장된 암호와 일치하는지 검증하는 데 사용하는 인터페이스입니다.
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            //isPresent(): 값이 들어있는지 여부를 true/false로 확인합니다. (다만, 이 메서드를 통한 if문 분기는 옛날 방식이므로 orElseThrow나 ifPresent 사용을 더 권장합니다.) 
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
