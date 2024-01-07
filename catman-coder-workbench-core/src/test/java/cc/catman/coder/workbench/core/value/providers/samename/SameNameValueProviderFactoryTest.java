package cc.catman.coder.workbench.core.value.providers.samename;

import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.parameter.strategy.ArrayParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.MapParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.RawParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.StructParameterParseStrategy;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.coder.workbench.core.utils.MapBuilder;
import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContext;
import cc.catman.coder.workbench.core.value.executor.DefaultValueProviderExecutor;
import cc.catman.coder.workbench.core.value.providers.simple.SimpleValueProviderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.ArrayList;
import java.util.List;

class SameNameValueProviderFactoryTest {

    @Test
    @SneakyThrows
    void create() {
        ValueProviderDefinition vpd = ValueProviderDefinition.builder().kind("samename")
                .args(Parameter.builder().name("name")
                        .type(TypeDefinition.builder()
                                .type(new StringRawType())
                                .build())
                        .value(ValueProviderDefinition.builder().kind("simple").config("firstName").build())
                        .build())
                .build();

        List<IParameterParseStrategy> strategies=new ArrayList<>();
        strategies.add(new RawParameterParseStrategy());
        strategies.add(new ArrayParameterParseStrategy());
        strategies.add(new MapParameterParseStrategy());
        strategies.add(new StructParameterParseStrategy());

        ValueProviderContext valueProviderContext= DefaultValueProviderContext.builder()
                .variables(MapBuilder.<String,Object>create().add("firstName","catman").build())
                .valueProviderRegistry(new ValueProviderRegistry()
                        .add("simple",new SimpleValueProviderFactory())
                        .add("samename",new SameNameValueProviderFactory())
                )
                .valueProviderExecutor(new DefaultValueProviderExecutor(strategies))
                .genericConversionService(new GenericConversionService())
                .build();

        Object res = valueProviderContext.exec(vpd);
        assert "catman".equals(res);
    }
}