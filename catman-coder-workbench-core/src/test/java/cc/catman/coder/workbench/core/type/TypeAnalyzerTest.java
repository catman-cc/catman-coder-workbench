package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.type.complex.StructType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class TypeAnalyzerTest {

    static class HttpRequest{
        String url;
        String method;
        String body;
        Map<String, List<String>> headers;
    }

    @Test
    void analyzer() {
        DefaultType analyzer = TypeAnalyzer.builder()
                        .typeObject(HttpRequest.class)
                        .build().analyzer();
        assert analyzer instanceof StructType;
        assert analyzer.getPrivateItems().size() == 4;
    }

}