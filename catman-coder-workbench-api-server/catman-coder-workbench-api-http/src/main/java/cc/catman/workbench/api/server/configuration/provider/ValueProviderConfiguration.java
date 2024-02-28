package cc.catman.workbench.api.server.configuration.provider;

import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.ArrayParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.MapParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.RawParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.StructParameterParseStrategy;
import cc.catman.coder.workbench.core.type.SimpleTypeAnalyzer;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.TypeDefinitionAnalyzer;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;
import cc.catman.coder.workbench.core.value.ValueProviderRegistry;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContextManager;
import cc.catman.coder.workbench.core.value.executor.DefaultValueProviderExecutor;
import cc.catman.coder.workbench.core.value.providers.expressions.ExpressionsValueProvider;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderArgs;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderFactory;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderResult;
import cc.catman.coder.workbench.core.value.providers.samename.SameNameValueProviderFactory;
import cc.catman.coder.workbench.core.value.template.DefaultValueProviderTemplate;
import cc.catman.coder.workbench.core.value.template.DefaultValueProviderTemplateManager;
import cc.catman.coder.workbench.core.value.template.ValueProviderTemplate;
import cc.catman.coder.workbench.core.value.template.ValueProviderTemplateManager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
public class ValueProviderConfiguration {
    @Resource
    private GenericConversionService convertService;

    @Bean
    public ValueProviderRegistry valueProviderRegistry(){
        ValueProviderRegistry valueProviderRegistry = new ValueProviderRegistry();
        valueProviderRegistry.add("http", new HttpValueProviderFactory());
        valueProviderRegistry.add("sameName", new SameNameValueProviderFactory());
        return valueProviderRegistry;
    }
    @Bean
    public ValueProviderExecutor executor() {
        convertService.addConverter(HashMap.class, HttpValueProviderArgs.class, source-> new ModelMapper().map(source, HttpValueProviderArgs.class));

        List<IParameterParseStrategy> strategies=new ArrayList<>();
        strategies.add(new RawParameterParseStrategy());
        strategies.add(new ArrayParameterParseStrategy());
        strategies.add(new MapParameterParseStrategy());
        strategies.add(new StructParameterParseStrategy());

        ValueProviderExecutor executor = DefaultValueProviderExecutor.builder()
                .genericConversionService(convertService)
                .contextManager(new DefaultValueProviderContextManager())
                .valueProviderRegistry(valueProviderRegistry())
                .parameterParseStrategies(strategies)
                .build();
        return executor;
    }

    @Bean
    public ValueProviderTemplateManager valueProviderTemplateManager() {
        return DefaultValueProviderTemplateManager.builder().build()
                .register(expressionProvider())
                .register(sameNameProvider())
                .register(httpProvider())
                ;

    }

    /**
     * 添加表达式提取器
     */
    protected ValueProviderTemplate expressionProvider() {
        TypeDefinition args = SimpleTypeAnalyzer.of(SimpleTypeAnalyzer.TypeDesc.create()
                .type("anonymous")
                .name("args")
                .add("language", "string")
                .add("expression", "string")
        ).analyzer();

        return DefaultValueProviderTemplate.builder()
                .id(ExpressionsValueProvider.class.getName())
                .name("表达式")
                .kind("expressions")
                .description("基于表达式从上下文获取值,默认支持SpEL表达式")
                .args(args)
                .result(TypeDefinitionAnalyzer
                        .builder()
                        .name("result")
                        .object(Object.class)
                        .build().analyze())
                .build();
    }

    protected ValueProviderTemplate sameNameProvider() {
        TypeDefinition args = SimpleTypeAnalyzer.of(SimpleTypeAnalyzer.TypeDesc.create()
                .type("value")
                .name("string")
        ).analyzer();

        return DefaultValueProviderTemplate.builder()
                .id(ExpressionsValueProvider.class.getName())
                .name("同名参数")
                .kind("sameName")
                .description("从当前上下文中获取同名参数")
                .args(args)
                .result(TypeDefinitionAnalyzer
                        .builder()
                        .name("result")
                        .object(Object.class)
                        .build().analyze())
                .build();
    }

    protected ValueProviderTemplate httpProvider() {
        TypeDefinition args = TypeDefinitionAnalyzer.builder()
                .name("args")
                .object(HttpValueProviderArgs.class)
                .build()
                .analyze();

        TypeDefinition result=TypeDefinitionAnalyzer.builder()
                .name("result")
                .object(HttpValueProviderResult.class)
                .build()
                .analyze();

        return DefaultValueProviderTemplate.builder()
                .id(ExpressionsValueProvider.class.getName())
                .name("http")
                .kind("http")
                .description("调用http请求")
                .args(args)
                .result(result)
                .build();
    }
}
