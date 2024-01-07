package cc.catman.coder.workbench.core;

import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.ArrayParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.MapParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.RawParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.strategy.StructParameterParseStrategy;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContextManager;
import cc.catman.coder.workbench.core.value.executor.DefaultValueProviderExecutor;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;
import cc.catman.coder.workbench.core.value.ValueProviderRegistry;
import cc.catman.coder.workbench.core.value.providers.expressions.ExpressionsValueProviderFactory;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderArgs;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderFactory;
import cc.catman.coder.workbench.core.value.providers.samename.SameNameValueProviderFactory;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValueProviderExecutorUtils {
    public static ValueProviderExecutor getExecutor() {
        GenericConversionService convertService = (GenericConversionService) DefaultConversionService.getSharedInstance();
        convertService.addConverter(HashMap.class, HttpValueProviderArgs.class, source-> new ModelMapper().map(source, HttpValueProviderArgs.class));

        ValueProviderRegistry valueProviderRegistry = new ValueProviderRegistry();
        valueProviderRegistry.add("http", new HttpValueProviderFactory());
        valueProviderRegistry.add("sameName", new SameNameValueProviderFactory());
        valueProviderRegistry.add("expressions", new ExpressionsValueProviderFactory());

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
}
