package springboot.example.mysite;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
//get 메서드 자동 생성
@Setter
//set 메서드 자동 생성
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "groomgroom", length = 200)
//    private String subject;

    @Column(name = "groomigroom",columnDefinition = "TEXT")
    private String content;

    @Column(name = "goooroooorom")
    private LocalDateTime createDate;

    @ManyToOne
    private Question question;
}
