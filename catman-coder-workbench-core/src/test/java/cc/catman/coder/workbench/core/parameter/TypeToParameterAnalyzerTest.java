package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.JSONMapper;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeAnalyzer;
import cc.catman.coder.workbench.core.type.complex.StructType;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypeToParameterAnalyzerTest {

    static class HttpRequest{
        String url;
        String method;
        String body;
        Map<String, List<String>> headers;
    }

    @Test
    @SneakyThrows
    void analyzer() {
        DefaultType typeOne = TypeAnalyzer.builder()
                        .typeObject(HttpRequest.class)
                        .build().analyzer();

        Parameter p1 = TypeToParameterAnalyzer.builder().object(typeOne).build().analyzer();
        Parameter p2=TypeToParameterAnalyzer.builder().object(HttpRequest.class).build().analyzer();

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.url="http://www.baidu.com";
        httpRequest.method="GET";
        httpRequest.body="body";
        httpRequest.headers=Map.of("key",List.of("value"));

        Parameter p3=TypeToParameterAnalyzer.builder().object(httpRequest).build().analyzer();

        DefaultType p1t = p1.getType().getType();
        DefaultType p2t = p2.getType().getType();
        DefaultType p3t = p3.getType().getType();

        assert p1t.canConvert(p2t);
        assert p1t.canConvert(p3t);
        assert p2t.canConvert(p3t);
        assert p1t.isType(p2t);
        assert p1t.isType(p3t);
        assert p2t.isType(p3t);

        System.out.println( new JsonMapper().writeValueAsString(p3));
    }
}