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
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
