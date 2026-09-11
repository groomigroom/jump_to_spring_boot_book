package springboot.example.mysite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository <Answer, Integer> {

}
