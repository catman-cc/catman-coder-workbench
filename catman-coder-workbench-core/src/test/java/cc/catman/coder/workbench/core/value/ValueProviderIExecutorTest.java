package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.parameter.TypeToParameterAnalyzer;
import cc.catman.coder.workbench.core.parameter.strategy.*;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.utils.MapBuilder;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContextManager;
import cc.catman.coder.workbench.core.value.executor.DefaultValueProviderExecutor;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProvider;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderArgs;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderFactory;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderResult;
import cc.catman.coder.workbench.core.value.providers.samename.SameNameValueProviderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


class ValueProviderIExecutorTest {
    @Test
    void run() {
        List<IParameterParseStrategy> strategies=new ArrayList<>();
        strategies.add(new RawParameterParseStrategy());
        strategies.add(new ArrayParameterParseStrategy());
        strategies.add(new MapParameterParseStrategy());
        strategies.add(new StructParameterParseStrategy());
        strategies.add(new AnonymousParameterParseStrategy());


        ValueProviderExecutor executor= DefaultValueProviderExecutor.builder()
                .parameterParseStrategies(strategies)
                .genericConversionService((GenericConversionService) DefaultConversionService.getSharedInstance())
                .build();

        HttpValueProviderResult res=executor.exec(HttpValueProvider.builder()
                .args(
                        HttpValueProviderArgs.builder()
                                .url("https://www.baidu.com")
                                .build()
                ).build(),HttpValueProviderResult.class);
        System.out.println(res);
    }

    @Test
    void run2() {

        ValueProviderExecutor executor = getExecutor();

        Parameter args = Parameter.builder()
                .name("args")
                .type(TypeDefinition.builder()
                        .name("args")
                        .type(DefaultType.builder().typeName("map").build())
                        .build())
                .value(ValueProviderDefinition.builder()
                        .kind("sameName")
                        .name("sameName")
                        .args(Parameter.builder()
                                .name("name")
                                .type(TypeDefinition.builder()
                                        .name("name")
                                        .type(DefaultType.builder().typeName("string").build())
                                        .build())
                                .build())
                        .build())
                .items(Arrays.asList(
                        Parameter.builder()
                                .name("url")
                                .type(TypeDefinition.builder()
                                        .name("url")
                                        .type(DefaultType.builder().typeName("string").build())
                                        .build())
                                .build(),
                        Parameter.builder()
                                .name("method")
                                .type(TypeDefinition.builder()
                                        .name("method")
                                        .type(DefaultType.builder().typeName("string").build())
                                        .build())
                                .build(),
                        Parameter.builder()
                                .name("body")
                                .type(TypeDefinition.builder()
                                        .name("body")
                                        .type(DefaultType.builder().typeName("string").build())
                                        .build())
                                .build(),
                        Parameter.builder()
                                .name("headers")
                                .type(TypeDefinition.builder()
                                        .name("headers")
                                        .type(DefaultType.builder().typeName("map").build())
                                        .build())
                                .build(),
                        Parameter.builder()
                                .name("settings")
                                .type(TypeDefinition.builder()
                                        .name("settings")
                                        .type(DefaultType.builder().typeName("map").build())
                                        .build())
                                .build()
                ))
                .build();

        ValueProviderDefinition httpValueProviderDefinition = ValueProviderDefinition.builder()
                .kind("http")
                .name("http")
                .args(args)
                .build();

        HttpValueProviderResult res = executor.exec(httpValueProviderDefinition
                , MapBuilder.<String,Object>create()
                                .add("args",MapBuilder.<String,Object>create()
                                        .add("url","https://www.baidu.com")
                                        .build())
                        .build()
                ,HttpValueProviderResult.class);
        System.out.println(res);
    }

    private static ValueProviderExecutor getExecutor() {
        GenericConversionService convertService = (GenericConversionService) DefaultConversionService.getSharedInstance();
        convertService.addConverter(HashMap.class, HttpValueProviderArgs.class, source-> new ModelMapper().map(source, HttpValueProviderArgs.class));

        ValueProviderRegistry valueProviderRegistry = new ValueProviderRegistry();
        valueProviderRegistry.add("http", new HttpValueProviderFactory());
        valueProviderRegistry.add("sameName", new SameNameValueProviderFactory());

        List<IParameterParseStrategy> strategies=new ArrayList<>();
        strategies.add(new RawParameterParseStrategy());
        strategies.add(new ArrayParameterParseStrategy());
        strategies.add(new MapParameterParseStrategy());
        strategies.add(new StructParameterParseStrategy());

        ValueProviderExecutor executor = DefaultValueProviderExecutor.builder()
                .genericConversionService(convertService)
                .contextManager(new DefaultValueProviderContextManager())
                .valueProviderRegistry(valueProviderRegistry)
                .parameterParseStrategies(strategies)
                .build();
        return executor;
    }

    @Test
    @SneakyThrows
    void run3(){
        ValueProviderExecutor executor = getExecutor();
        TypeToParameterAnalyzer analyzer = TypeToParameterAnalyzer.builder()
                .object(HttpValueProviderArgs.class)
                .name("args")
                .forceType(Constants.Type.TYPE_NAME_ANONYMOUS)
                .build();
        Parameter args = analyzer.analyzer();

//        System.out.println(new ObjectMapper().writeValueAsString(args));
        ValueProviderDefinition httpValueProviderDefinition = ValueProviderDefinition.builder()
                .kind("http")
                .name("http")
                .args(args)
                .build();
        HttpValueProviderResult res = executor.exec(httpValueProviderDefinition
                , MapBuilder.<String,Object>create()
                        .add("args",MapBuilder.<String,Object>create()
                                .add("url","https://www.baidu.com")
                                .build())
                        .build()
                ,HttpValueProviderResult.class);
        assert res.getStatusCode()==200;
    }
}