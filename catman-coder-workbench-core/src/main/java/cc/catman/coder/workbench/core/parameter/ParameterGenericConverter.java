package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.type.DefaultType;
import lombok.SneakyThrows;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * 参数转换器
 */
public class ParameterGenericConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair( Parameter.class,Object.class));
    }

    @Override
    @SneakyThrows
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        // 参数转对象,只做参数转对象这一步操作,内部参数递归处理
        Object res = targetType.getType().newInstance();
        Parameter parameter = (Parameter) source;

        DefaultType type = parameter.getType().getType();
        return null;
    }
}
