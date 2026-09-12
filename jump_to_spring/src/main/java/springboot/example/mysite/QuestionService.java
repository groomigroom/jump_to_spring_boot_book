package springboot.example.mysite;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
//controller와 repository를 연결해 주는 거
public class QuestionService {
    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            //Optional 객체 내부에 값이 존재하는지 여부를 확인하는 메서드입니다.
            return question.get();
        } else {
            throw new DataNotFoundException("질문이 안 찾아집니다.");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        //Pageable 페이징을 처리하는 인터페이스이다.
        //PageRequest 현재 페이지와 한 페이지에 보여 줄 게시물 개수 등을 설정하여 페이징 요청을 하는 클래스이다.
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}


/*
@Query 애너테이션 사용하기
이 내용은 이미 검색 기능을 구현했으므로 중복될 수 있지만, 앞으로 스프링( Spring) 부트를 활용해 웹 개발을 하려는 여러분에게 도움이 되고자 보충 설명하는 것이다. 꼭 따라 하지 않아도 좋다. 쿼리에 익숙하다면 이와 같이 자바 코드로 쿼리를 생성하는 방식보다 직접 쿼리를 작성하는 게 훨씬 편하게 여겨질 것이다. 이번에는 Specification 대신 쿼리를 직접 작성하여 검색 기능을 구현하는 방법을 간단히 알아보자.

1) QuestionRepository에 다음과 같은 메서드를 추가해 보자.

[파일명:/question/QuestionRepository.java]

(... 생략 ...)
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    (... 생략 ...)

    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
여기서는 @Query 애너테이션이 적용된 findAllByKeyword 메서드를 추가했다. 앞에서 살펴본 쿼리를 @Query로 구현한 것이다. 이때 @Query는 반드시 테이블 기준이 아닌 엔티티 기준으로 작성해야 한다. 즉, site_user와 같은 테이블명 대신 SiteUser처럼 엔티티명을 사용해야 하고, 조인문에서 보듯이 q.author_id=u1.id와 같은 컬럼명 대신 q.author=u1처럼 엔티티의 속성명을 사용해야 한다.

그리고 @Query에 매개변수로 전달할 kw 문자열은 메서드의 매개변수에 @Param("kw")처럼 @Param 애너테이션을 사용해야 한다. 검색어를 의미하는 kw 문자열은 @Query 안에서 :kw로 참조된다.

2) 작성한 findAllByKeyword 메서드를 사용하기 위해 QuestionService를 다음과 같이 수정하자.

[파일명:/question/QuestionService.java]

(... 생략 ...)
public class QuestionService {

    (... 생략 ...)

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }

    (... 생략 ...)
}
 */
