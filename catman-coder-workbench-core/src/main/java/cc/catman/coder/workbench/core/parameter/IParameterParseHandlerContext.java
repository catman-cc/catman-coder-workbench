package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Map;

/**
 * 参数解析处理器上下文
 *
 *
 */
public interface IParameterParseHandlerContext {
    Map<String,Object> buildVariables();

    ValueProviderContext getValueProviderContext();

    IParameterParseHandlerContext registerParameterParseStrategy(IParameterParseStrategy strategy);

   default  Object parse(Parameter parameter){
       return  parse(parameter,Object.class);
   }

   <T> T parse(Parameter parameter, TypeDescriptor descriptor);

   default <T> T parse(Parameter parameter,Class<T> clazz){
         return parse(parameter,TypeDescriptor.valueOf(clazz));
   }
}
