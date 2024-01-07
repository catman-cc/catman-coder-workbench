package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.utils.MapBuilder;
import cc.catman.coder.workbench.core.value.report.ReportMessage;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ValueProviderContext {

    /**
     * 获取上下文的ID,该ID用于在调试时标识上下文
     * @return 上下文Id
     */
    String getId();

    /**
     * 尽可能生成一个可读性强的上下文名称
     */
    String getName();

    /**
     * 获取上下文的批次ID,该ID用于在调试时标识上下文
     * @return 上下文批次Id
     */
    String getBatchId();

    Map<String,Object> variables();

    Map<String,Object> buildVariables();

    Optional<ValueProviderContext> getParentContext();

    IValueProviderContextManager getContextManager();

    ValueProviderRegistry getValueProviderRegistry();

    List<IParameterParseStrategy> getParameterParseStrategies();

    <T> T convert(Object value, TypeDescriptor type);

    /**
     * 创建表达式解析器
     * @param language 表达式语言
     * @return 表达式解析器
     */
    ExpressionParser createExpressionParser(String language);

    default  EvaluationContext createEvaluationContext(){
        return createEvaluationContext(new HashMap<>());
    }
    EvaluationContext createEvaluationContext(Map<String,Object> variables);

    GenericConversionService conversionService();

    /**
     * 获取上下文中的变量
     * @return 变量
     */
    Map<String,Object> getVariables();

    Object getVariable(String name);

    default void breakPoint() {

    }

    default void breakPoint(String name,Map<String,Object> variables) {

    }

    default void report(ReportMessage<?> message) {
        // 整个任务的处理过程中,需要进行多次上下文的汇报,其中包括上下文的创建,上下文的销毁,上下文的变量变更等
    }
    default void report(Supplier<ReportMessage<?>> messageSupplier) {

    }


    default <T> T getVariable(String name,Class<T> type){
        return getVariable(name,TypeDescriptor.valueOf(type));
    }

   default  <T> T getVariable(String name,TypeDescriptor typeDescriptor){
        Object value = getVariable(name);
        return convert(value,typeDescriptor);
   }

    void addVariable(String name,Object value);

    default <T> T parse(Parameter parameter, Class<T> type){
        return parse(parameter,TypeDescriptor.valueOf(type));
    }

    <T> T parse(Parameter parameter, TypeDescriptor descriptor);

    ValueProvider parse(ValueProviderDefinition valueProviderDefinition,ValueProviderContext context,Map<String,Object> presetVariables);

   default ValueProvider parse(ValueProviderDefinition valueProviderDefinition,ValueProviderContext context){
         return parse(valueProviderDefinition,context,new HashMap<>());
   }
   default ValueProvider parse(ValueProviderDefinition valueProviderDefinition){
       return parse(valueProviderDefinition,this);
   }

    default ValueProvider parse(ValueProviderDefinition valueProviderDefinition,Map<String,Object> presetVariables){
        return parse(valueProviderDefinition,this,presetVariables);
    }



    default ValueProviderContext createChildContext(Map<String,Object> variables){
        return createChildContext(null,variables);
    }

    default ValueProviderContext createChildContext(){
        return createChildContext(new HashMap<>());
    }

    default ValueProviderContext createChildContext(ValueProvider valueProvider){
        return createChildContext(valueProvider,new HashMap<>());
    }

    ValueProviderContext createChildContext(ValueProvider valueProvider,Map<String,Object> variables);

    default PropertyAccessor createPropertyAccessor(Object target){
//        return PropertyAccessorFactory.forDirectFieldAccess(
//                target
//        );
        return PropertyAccessorFactory.forBeanPropertyAccess(target);
    }
    IParameterParseHandlerContext createParameterParseHandlerContext();

    ValueProviderExecutor getValueProviderExecutor();

    default Object  exec(ValueProvider valueProvider){
        return this.exec(valueProvider,new HashMap<>());
    }

    default Object  exec(ValueProvider valueProvider,Map<String,Object> presetVariables){
        return getValueProviderExecutor().exec(valueProvider,this,presetVariables);
    }
    default <T> T  exec(ValueProvider valueProvider,Class<T> type){
        return exec(valueProvider,TypeDescriptor.valueOf(type));
    }

    default <T> T  exec(ValueProvider valueProvider,Class<T> type,Map<String,Object> presetVariables){
        return exec(valueProvider,TypeDescriptor.valueOf(type),presetVariables);
    }
    /**
     * 在当前上下文中执行其他的值提供者,该操作会创建一个子上下文
     * @param valueProvider 值提供者
     * @return 执行结果,如果执行失败,则返回null. 当执行完毕后,子上下文会被销毁
     */
    default <T> T  exec(ValueProvider valueProvider,TypeDescriptor typeDescriptor){
        this.report(()->ReportMessage.builder()
                .sourceType("context")
                .sourceId(this.getId())
                .batchId(this.getBatchId())
                .eventKind("pre-exec-value-provider")
                .data(MapBuilder.create()
                        .add("valueProvider",valueProvider.getId())
                        .build()
                )
                .build());
        Object res = this.exec(valueProvider);
        this.report(()->ReportMessage.builder()
                .sourceType("context")
                .sourceId(this.getId())
                .batchId(this.getBatchId())
                .eventKind("post-exec-value-provider")
                .data(MapBuilder.create()
                        .add("valueProvider",valueProvider.getId())
                        .add("result",res)
                        .build()
                )
                .build());
        return convert(res,typeDescriptor);
    }

    default <T> T  exec(ValueProvider valueProvider,TypeDescriptor typeDescriptor,Map<String,Object> presetVariables){
        Object res = this.exec(valueProvider,presetVariables);
        return convert(res,typeDescriptor);
    }


    default Object exec(ValueProviderDefinition valueProviderDefinition){
        return exec(valueProviderDefinition,Object.class,new HashMap<>());
    }
    default <T> T exec(ValueProviderDefinition valueProviderDefinition,Class<T> type){
        return exec(valueProviderDefinition,type,new HashMap<>());
    }

    default <T> T exec(ValueProviderDefinition valueProviderDefinition,Class<T> type,Map<String,Object> presetVariables){
        this.report(()->ReportMessage.builder()
                .sourceType("context")
                .sourceId(this.getId())
                .batchId(this.getBatchId())
                .eventKind("parse-value-provider")
                .data(MapBuilder.create()
                        .add("valueProviderDefinition",valueProviderDefinition.getId())
                        .add("presetVariables",presetVariables)
                        .build()
                )
                .build());
        ValueProvider valueProvider = parse(valueProviderDefinition,this,presetVariables);
        this.report(()->ReportMessage.builder()
                .sourceType("context")
                .sourceId(this.getId())
                .batchId(this.getBatchId())
                .eventKind("parse-value-provider-end")
                .data(MapBuilder.create()
                        .add("valueProvider",valueProvider.getId())
                        .build()
                )
                .build());
        return exec(valueProvider,type);
    }

    default <T> T exec(ValueProviderDefinition valueProviderDefinition,TypeDescriptor typeDescriptor){
        ValueProvider valueProvider = parse(valueProviderDefinition,this);
        return exec(valueProvider,typeDescriptor);
    }
    default <T> T exec(ValueProviderDefinition valueProviderDefinition,TypeDescriptor typeDescriptor,Map<String,Object> presetVariables){
        ValueProvider valueProvider = parse(valueProviderDefinition,this,presetVariables);
        return exec(valueProvider,typeDescriptor);
    }

    default ValueProviderContext register(){
        getContextManager().addContext(this);
        return this;
    }
}
