package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.value.ValueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认的参数解析处理器上下文
 */
public class DefaultIParameterParseHandlerContext implements IParameterParseHandlerContext {
    /**
     * 上下文中对应的值
     */
    private Map<String,Object> variables;

    /**
     * 当前上下文所属的父上下文
     */
    private Optional<IParameterParseHandlerContext> parentContext=Optional.empty();

    public Map<String,Object> buildVariables(){
        // 构建上下文时,所进行的操作是依次获取父上下文中的所有变量,之后再由当前上下文中的变量覆盖父上下文中的变量
        // 由于父上下文中的变量是不可变的,所以这里使用的是一个新的Map来存储变量
        Map<String,Object> variables=parentContext
                .map(IParameterParseHandlerContext::buildVariables)
                .orElseGet(HashMap::new);
        // 将当前上下文中的变量覆盖父上下文中的变量
        variables.putAll(this.variables);
        return variables;
    }

    public Parameter parseParameter(Parameter parameter){

        return Optional.of(parameter).map(param->{
            ValueProvider<?> value = Optional.ofNullable(param.getValue()).orElseGet(()->param.getDefaultValue());

            if(value!=null){
                param.setValue(value.parse(this));
            }

        }).orElse(null);
    }
}
