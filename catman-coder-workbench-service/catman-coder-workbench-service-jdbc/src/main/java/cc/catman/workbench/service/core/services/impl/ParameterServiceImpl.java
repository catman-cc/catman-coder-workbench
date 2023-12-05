package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.json.JsonValueProvider;
import cc.catman.coder.workbench.core.value.json.JsonValueProviderConfig;
import cc.catman.coder.workbench.core.value.parent.ParentValueProvider;
import cc.catman.coder.workbench.core.value.parent.ParentValueProviderConfig;
import cc.catman.coder.workbench.core.value.simple.SimpleValueProvider;
import cc.catman.coder.workbench.core.value.simple.SimpleValueProviderConfig;
import cc.catman.workbench.service.core.services.IParameterService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class ParameterServiceImpl implements IParameterService {
    @Override
    public Optional<Parameter> createFromTypeDefinition(TypeDefinition typeDefinition) {
       return  createFromTypeDefinition(null,typeDefinition);
    }

    @Override
    public Optional<Parameter> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Parameter save(Parameter parameter) {
        return null;
    }

    @Override
    public Optional<Parameter> deleteById(String id) {
        return Optional.empty();
    }


    public Optional<Parameter> createFromTypeDefinition(TypeDefinition parent,TypeDefinition typeDefinition) {
        // 遍历类型定义的结构
       return Optional.ofNullable(typeDefinition).map(td -> {
            // 创建Parameter实例
            return  Parameter.builder()
                    .name(td.getName())
                    .describe(td.getDescribe())
                    .type(td)
                    .items(Optional.of(td.getType().getItems())
                            .map(itds-> itds
                                    .stream()
                                    .map(itd -> createFromTypeDefinition(td,itd).orElse(null))
                                    .filter(Objects::nonNull)
                                    .toList()
                            )
                            .orElse(Collections.emptyList()))
                    .value(createValueProvider(parent,typeDefinition))
                    .build();
        });
    }



    /**
     * 为参数创建值提供者,如果类型定义是一个基本类型,则创建一个默认值提供者
     * 如果类型定义所属的定义是一个复合类型,则创建一个父值提供者
     *
     * @param parent         父级参数定义
     * @param typeDefinition 类型定义
     * @return 值提供者
     */
    protected ValueProvider<?> createValueProvider(TypeDefinition parent, TypeDefinition typeDefinition) {
        return Optional.ofNullable(parent).<ValueProvider<?>>map(p -> {
            // 如果父级参数定义不为空,则创建一个父值提供者
            // 如果父级参数定义是一个数组,那么当前定义只能是elements
            // 理论上elements的作用是提供子元素类型定义
            // 在这里elements的处理和其余的参数处理不一致,其本质上应该处理循环中的元素
            // 所以隐式包含了一个循环数组的特性
            // 但是这里不应该由ValueProvider去处理数组逻辑,所以应该交给谁呢?
            // 解决方案: 交给上下文构建器去处理,为array单独构建一次循环上下文
            // 所以就不需要单独配置array了,直接默认是用父元素提取器就可以刻

            // 只要由父级结构,默认使用父元素提取器
            return ParentValueProvider.builder()
                    .config(ParentValueProviderConfig.builder().build())
                    .build();
        }).orElseGet(() -> {
            // 如果父级参数定义为空,表示当前参数定义是一个根参数定义
            // 此时需要根据类型来决定创建什么样的值提供者
            // 比如:如果类型是一个基本类型,则创建一个默认值提供者
            // 如果类型是一个复合类型,则创建一个父值提供者
            if (typeDefinition.getType().isComplex()) {
                return JsonValueProvider.builder()
                        .config(JsonValueProviderConfig.builder().sourceKind("raw").build())
                        .build();
            }
            return SimpleValueProvider.builder()
                    .kind(SimpleValueProvider.KIND)
                    .config(SimpleValueProviderConfig.builder()
                            .build())
                    .build();
        });
    }
}
