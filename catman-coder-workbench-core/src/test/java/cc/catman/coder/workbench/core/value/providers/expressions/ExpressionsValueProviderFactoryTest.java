package cc.catman.coder.workbench.core.value.providers.expressions;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.ValueProviderExecutorUtils;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.parameter.TypeToParameterAnalyzer;
import cc.catman.coder.workbench.core.type.AnonymousType;
import cc.catman.coder.workbench.core.utils.MapBuilder;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;
import org.junit.jupiter.api.Test;

import java.util.Map;


class ExpressionsValueProviderFactoryTest {

    @Test
    void run() {
        ValueProviderExecutor executor = ValueProviderExecutorUtils.getExecutor();

        TypeToParameterAnalyzer analyzer = TypeToParameterAnalyzer.builder()
                .forceType(Constants.Type.TYPE_NAME_ANONYMOUS)
                .object(AnonymousType.class)
                .build();

        Parameter parameter = analyzer.analyzer();
        parameter.addItem(TypeToParameterAnalyzer.builder()
                .object("")
                .name("language")
                .build().analyzer()
        );
        parameter.addItem(TypeToParameterAnalyzer.builder()
                .object("")
                .name("expression")
                .build().analyzer()
        );

        ValueProviderDefinition expressionProviderDefinition = ValueProviderDefinition.builder()
                .kind("expressions")
                .name("expressions")
                .args(parameter)
                .build();
        Map<String,Object> variables = MapBuilder.<String,Object>create()
                .put("language","spel")
                .put("expression","1+1")
                .build();

        assert  2==executor.exec(expressionProviderDefinition,variables , Integer.class);
    }
}