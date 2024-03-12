package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;

import java.util.*;

/**
 * 数组参数解析策略
 * 解析数据参数策略时,分为了两个环节:
 * - 数组本身对应到取值函数
 * - 数组内部元素的取值函数
 * 比如: 现有下面的数组定义
 * - name: "array"
 *   value: $array
 *   elements:
 *     type: "string"
 *     value: $each-1
 * 对应的变量表是:
 * - $array: [1,2,3,4,5]
 * 首先进行$array的解析,获取到数组[1,2,3,4,5],然后对数组中的每一个元素进行解析,获取到最终的结果:[0,1,2,3,4]
 */
public class ArrayParameterParseStrategy extends AbstractParameterParseStrategy{
    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        // 获取取值函数
        FunctionCallInfo fci=Optional.ofNullable(parameter.getValueFunction()).orElse(parameter.getDefaultValueFunction());
        if (Optional.ofNullable(fci).isEmpty()){
            // 参数没有配置任何提取器,则直接返回null,因为如果没有取值器,我无法判断数组的长度
            return null;
        }

        // 构建预置信息,将当前参数名称放入预置信息中,这样在解析取值函数时,就可以使用该参数名称,从而获取到参数的值
//        Map<String,Object> preset = new HashMap<>();
//        preset.put("__parameter__name__",parameter.getName());
        Object preparseCollection = fci.call(stack);

        if (Objects.isNull(preparseCollection)){
            if (parameter.isSkipChildFlag()){
                if (parameter.isRequired()){
                    throw new RuntimeException("参数"+parameter.getName()+"的值提取器返回的值为空,但是该参数是必须的");
                }
            }
            return null;
        }

        // 构建element的取值上下文
        // 校验数组必须是一个集合或者数组类型
        if (!(preparseCollection instanceof Object[] || preparseCollection instanceof Collection)){
            throw new RuntimeException("参数"+parameter.getName()+"的值提取器返回的值不是一个数组或者集合");
        }

        if (parameter.isSkipChildFlag()){
            // 如果跳过子参数,那么就直接返回预解析集合
            return preparseCollection;
        }

        // 作为数组定义,elements是有且唯一的,所以这里不需要判断是否为空
        // 解析数组中的每一个元素,并执行对应的取值函数,如果没有取值函数,则直接返回当前集合
        // 作为数组定义,elements是有且唯一的,所以这里不需要判断是否为空
        return parameter.get("elements")
                .filter(p-> Optional.ofNullable(p.getValueFunction()).orElse(p.getDefaultValueFunction()) != null)
                .map(elements -> {
            // 此时需要注意,elements所持有的值提取器,表示的是数组中每一个元素的值提取器
            // 此时,我应该如何处理呢?
            // 1. 首先,我需要知道数组的长度,这个长度是由其他参数决定的
            //    但是,因为缺乏语法分析,所以我无法知道数组的长度!!! 这是一个问题
            //    无论如何,当参数定义是数组类型时,其所属的值提取器必须是一个数组类型的值提取器,且数组长度由该值决定
            //    如果在执行了参数定义的值提取器之后,得到的数据不是一个数组,那么就会抛出异常!!!
            FunctionCallInfo elementsValueFunction = Optional.ofNullable(elements.getValueFunction()).orElse(elements.getDefaultValueFunction());

            List<Object> list = (List<Object>)stack.convertTo(preparseCollection, List.class);
            if (elements.isRequired()) {
                // 如果数组是必须的,那么就需要判断数组是否为空
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值为空,但是该参数是必须的");
                }
            }
            // 为子数据构建上下文数据
            Map<String, Object> presetVariables = new HashMap<>();
            presetVariables.put(parameter.getName(), preparseCollection);
            // 此时,我就可以获取数组长度,并开始解析了
            for (int i = 0; i < Objects.requireNonNull(list).size(); i++) {
                presetVariables.put("$list", list);
                presetVariables.put("$item", list.get(i));
                presetVariables.put("$parent", preparseCollection);
                presetVariables.put("$each", list.get(i));
                presetVariables.put("$index", i);
                presetVariables.put("$length", list.size());
                presetVariables.put("$first", i == 0);
                presetVariables.put("$last", i == list.size() - 1);
                presetVariables.put("$prev", i == 0 ? null : list.get(i - 1));
                // 构建子上下文,这里有一个问题,如果子变量定义是复合类型,那么需要进行的操作是通过
                IRuntimeStack childStack = stack.createChildStack(String.format("[%d]",i), elementsValueFunction, presetVariables);
                Object res = elementsValueFunction.call(childStack);
                list.set(i, res);
            }
            return list;
        }).orElse((List<Object>) preparseCollection);
    }

    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_ARRAY;
    }
}
