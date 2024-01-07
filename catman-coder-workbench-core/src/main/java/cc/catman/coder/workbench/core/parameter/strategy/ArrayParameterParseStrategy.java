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

/**
 * 数组参数解析策略
 */
public class ArrayParameterParseStrategy implements IParameterParseStrategy {
    @Override
    public boolean support(Parameter parameter) {
        DefaultType type = parameter.getType().getType();
        return type.isArray()||checkTypeName(parameter);
    }
    protected boolean checkTypeName(Parameter parameter){
        DefaultType type = parameter.getType().getType();
        return Constants.Type.TYPE_NAME_ARRAY.equals(type.getTypeName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parse(Parameter parameter, IParameterParseHandlerContext parameterParseHandlerContext) {
        ValueProviderDefinition vpd = Optional.ofNullable(parameter.getValue()).orElse(parameter.getDefaultValue());
        if (Optional.ofNullable(vpd).isEmpty()){
            // 参数没有配置任何提取器,则直接返回null,因为如果没有取值器,我无法判断数组的长度
            return null;
        }

        ValueProviderContext valueProviderContext = parameterParseHandlerContext.getValueProviderContext();
        // 构建预置信息,将当前参数名称放入预置信息中,这样在解析值提取器时,就可以使用该参数名称,从而获取到参数的值
        Map<String,Object> preset = new HashMap<>();
        preset.put("__parameter__name__",parameter.getName());

        // 在进行参数解析操作之前,需要网对应的上下文中,填充一些预置信息,这些预置信息,主要是为了方便参数解析时使用
        // 需要注意的是,在将定义转换为值提取器时,就会执行入参的值提取器,所以这里的预置信息,是在执行入参的值提取器之后,才会被填充的
        ValueProvider paramValueProvider = valueProviderContext.parse(vpd,preset);
        // 执行值提取器,获取值,此时已经获取到了参数的基础值,此处得到的是一个预解析集合
        // 该集合将作为数组的基础数据,用于构建数组
        Object preparseCollection = valueProviderContext.exec(paramValueProvider,preset);

        if (Objects.isNull(preparseCollection)){
            if (parameter.isSkipChildFlag()){
                if (parameter.isRequired()){
                    throw new RuntimeException("参数"+parameter.getName()+"的值提取器返回的值为空,但是该参数是必须的");
                }
            }
            return null;
        }

        // 校验数组必须是一个集合或者数组类型
        if (!(preparseCollection instanceof Object[] || preparseCollection instanceof Collection)){
           throw new RuntimeException("参数"+parameter.getName()+"的值提取器返回的值不是一个数组或者集合");
        }

        if (parameter.isSkipChildFlag()){
            // 如果跳过子参数,那么就直接返回预解析集合
            return preparseCollection;
        }
        // 作为数组定义,elements是有且唯一的,所以这里不需要判断是否为空
        return parameter.get("elements").map(elements -> {
            // 此时需要注意,elements所持有的值提取器,表示的是数组中每一个元素的值提取器
            // 此时,我应该如何处理呢?
            // 1. 首先,我需要知道数组的长度,这个长度是由其他参数决定的
            //    但是,因为缺乏语法分析,所以我无法知道数组的长度!!! 这是一个问题
            //    无论如何,当参数定义是数组类型时,其所属的值提取器必须是一个数组类型的值提取器,且数组长度由该值决定
            //    如果在执行了参数定义的值提取器之后,得到的数据不是一个数组,那么就会抛出异常!!!

            ValueProviderDefinition elementValueProviderDefinition = Optional.ofNullable(elements.getValue()).orElse(elements.getDefaultValue());
            if (Objects.isNull(elementValueProviderDefinition)) {
                // 如果没有定义值提取器,那么就直接返回预解析集合
                return preparseCollection;
            }

            List<Object> list = (List<Object>) valueProviderContext.conversionService().convert(preparseCollection, List.class);

            if (elements.isRequired()) {
                // 如果数组是必须的,那么就需要判断数组是否为空
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值为空,但是该参数是必须的");
                }
            }
            // 为子数据构建上下文数据
            Map<String, Object> presetVariables = new HashMap<>(valueProviderContext.buildVariables());
            presetVariables.put(parameter.getName(), preparseCollection);

            // 此时,我就可以获取数组长度,并开始解析了
            for (int i = 0; i < Objects.requireNonNull(list).size(); i++) {
                presetVariables.put("list", list);
                presetVariables.put("item", list.get(i));
                presetVariables.put("parent", preparseCollection);
                presetVariables.put("each", list.get(i));
                presetVariables.put("index", i);
                presetVariables.put("length", list.size());
                presetVariables.put("first", i == 0);
                presetVariables.put("last", i == list.size() - 1);
                presetVariables.put("prev", i == 0 ? null : list.get(i - 1));
                // 构建子上下文
                ValueProvider childValueProvider = valueProviderContext.getValueProviderRegistry()
                        .parse(elementValueProviderDefinition, valueProviderContext);
                // 执行值提取器,获取值
                ValueProviderContext childContext = valueProviderContext.createChildContext(childValueProvider, presetVariables);
                Object childValue = childContext.exec(childValueProvider);
                list.set(i, childValue);
            }
            return list;
        }).orElse(null);
    }
}
