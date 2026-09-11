package springboot.example.mysite;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
//get 메서드 자동 생성
@Setter
//set 메서드 자동 생성
@Entity
//db의 테이블과 연결하는 거
public class Answer {
    @Id
    //primary key에 매핑되는 거
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ////GenerationType.IDENTITY (가장 많이 사용) * 의미: 기본키 생성을 데이터베이스에 위임합니다. * 특징: MySQL의 AUTO_INCREMENT 처럼 데이터가 비어있는 상태로 저장(insert)되면 DB가 알아서 순차적인 번호를 부여합니다.
    private Integer id;

    @Column(name = "groomgroom", length = 200)
    private String subject;

    @Column(name = "groomigroom",columnDefinition = "TEXT")
    ////컬럼 이름과 columnDefinition은 sql 문구를 직접 작성하는 거
    private String content;

    @Column(name = "goooroooorom")
    private LocalDateTime createDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;
}
