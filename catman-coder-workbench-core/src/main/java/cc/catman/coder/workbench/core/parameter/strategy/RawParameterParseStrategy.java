package cc.catman.coder.workbench.core.parameter.strategy;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;

import java.util.*;

public class RawParameterParseStrategy implements IParameterParseStrategy {
    public static final List<String> RAW_TYPE_NAMES = new ArrayList<>(Arrays.asList(Constants.Type.TYPE_NAME_STRING
            ,Constants.Type.TYPE_NAME_NUMBER
            ,Constants.Type.TYPE_NAME_BOOLEAN));
    @Override
    public boolean support(Parameter parameter) {
        return parameter.getType().getType().isRaw()||checkTypeName(parameter);
    }

    protected boolean checkTypeName(Parameter parameter){
        DefaultType type = parameter.getType().getType();
        return RAW_TYPE_NAMES.contains(type.getTypeName());
    }

    @Override
    public Object parse(Parameter parameter, IParameterParseHandlerContext parameterParseHandlerContext) {
        // 解析参数,本质上就是迭代参数结构,然后根据每一个参数对应的值提取器来提取值的过程
        ValueProviderDefinition vpd = Optional.ofNullable(parameter.getValue()).orElse(parameter.getDefaultValue());
        if (Optional.ofNullable(vpd).isEmpty()){
            // 参数没有配置任何提取器,则直接返回null
            if (parameter.isRequired()){
                throw new RuntimeException("参数"+parameter.getName()+"的值提取器返回的值为空,但是该参数是必须的");
            }
            return null;
        }

        ValueProviderContext valueProviderContext = parameterParseHandlerContext.getValueProviderContext();
        Map<String,Object> preset = new HashMap<>();
        preset.put("__parameter__name__",parameter.getName());
        // 在进行参数解析操作之前,需要网对应的上下文中,填充一些预置信息,这些预置信息,主要是为了方便参数解析时使用
        // 需要注意的是,在将定义转换为值提取器时,就会执行入参的值提取器,所以这里的预置信息,是在执行入参的值提取器之后,才会被填充的
        ValueProvider paramValueProvider = valueProviderContext.parse(vpd,preset);
        // 执行值提取器,获取值,此时已经获取到了参数的基础值
        Object value = valueProviderContext.exec(paramValueProvider,preset);
        if (Objects.isNull(value)){
            if (parameter.isRequired()){
                throw new RuntimeException("参数"+parameter.getName()+"的值提取器返回的值为空,但是该参数是必须的");
            }
            return null;
        }
        return value;
    }
}
