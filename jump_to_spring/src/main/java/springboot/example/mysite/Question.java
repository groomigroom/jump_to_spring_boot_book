package springboot.example.mysite;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "groomgroom", length = 200)
    private String subject;

    @Column(name = "groomigroom",columnDefinition = "TEXT")
    private String content;

    @Column(name = "goooroooorom")
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question")
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;
}

/*
 create table question (
        id integer not null auto_increment,
        content TEXT,
        create_date datetime(6),
        subject varchar(200),
        primary key (id)
    ) engine=InnoDB
 */
