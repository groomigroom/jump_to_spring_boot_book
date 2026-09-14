package springboot.example.mysite;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
    public String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        //commonmark-java가 제공하는 구문 분석기(Parser) 클래스
        //.builder()는 빌더 패턴(Builder Pattern)을 구현하여 객체를 안전하고 가독성 있게 생성하는 메서드입니다.
        Node document = parser.parse(markdown);
        //Node는 마크다운 텍스트를 이루는 개별 구성 요소(제목, 문단, 링크, 이미지 등)를 추상화한 최상위 자바 객체(클래스)
        //parser.parse() 마크다운 문자열을 분석하여 객체 트리(Node)로 만듭니다.
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        //HtmlRenderer는 마크다운 구조(Node 트리)를 웹 브라우저가 읽을 수 있는 HTML 태그 문자열로 최종 변환(렌더링)해 주는 출력기 클래스입니다.
        return renderer.render(document);
        //renderer.render() Node 트리를 순수한 HTML 문자열로 최종 출력
    }
}
