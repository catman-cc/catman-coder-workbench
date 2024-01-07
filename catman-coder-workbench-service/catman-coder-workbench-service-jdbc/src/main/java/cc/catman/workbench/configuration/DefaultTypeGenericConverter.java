package cc.catman.workbench.configuration;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.type.AnyType;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.EnumType;
import cc.catman.coder.workbench.core.type.GenericType;
import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.MapType;
import cc.catman.coder.workbench.core.type.complex.ReferType;
import cc.catman.coder.workbench.core.type.complex.StructType;
import cc.catman.coder.workbench.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 默认类型转换器
 */
@Component
public class DefaultTypeGenericConverter implements GenericConverter {

    @Resource
    private ModelMapper modelMapper;

    private Set<ConvertiblePair> convertibleType = new HashSet<>();

    private Map<String, Class<?>> typeMap = new HashMap<>();

    public DefaultTypeGenericConverter() {
        this.addType(Constants.Type.TYPE_NAME_ARRAY, ArrayType.class)
                .addType(Constants.Type.TYPE_NAME_MAP, MapType.class)
                .addType(Constants.Type.TYPE_NAME_REFER, ReferType.class)
                .addType(Constants.Type.TYPE_NAME_STRUCT, StructType.class)
                .addType(Constants.Type.TYPE_NAME_BOOLEAN, BooleanRawType.class)
                .addType(Constants.Type.TYPE_NAME_STRING, StringRawType.class)
                .addType(Constants.Type.TYPE_NAME_NUMBER, NumberRawType.class)
                .addType(Constants.Type.TYPE_NAME_SLOT, DefaultType.class)
                .addType(Constants.Type.TYPE_NAME_ENUM, EnumType.class)
                .addType(Constants.Type.TYPE_NAME_ANY, AnyType.class)
                .addType(Constants.Type.TYPE_NAME_GENERIC, GenericType.class)
                .addType(Constants.Type.TYPE_NAME_FILE, FilterType.class)
        ;
    }

    public DefaultTypeGenericConverter addType(String typeName, Class<?> type) {
        typeMap.put(typeName, type);
        return this.addType(type);
    }

    protected DefaultTypeGenericConverter addType(Class<?> sourceType) {
        convertibleType.add(new ConvertiblePair(sourceType, DefaultType.class));
        convertibleType.add(new ConvertiblePair(DefaultType.class, sourceType));
        return this;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.convertibleType;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        // 根据类型定义配置转换器
        // 接下来就是类型转换了
        if (source instanceof DefaultType defaultType) {
            Class<?> targetClass = this.typeMap.get(defaultType.getTypeName());
            if (targetClass != null) {
                return modelMapper.map(defaultType, targetClass);
            }
            throw new RuntimeException("不支持的类型转换" + defaultType.getTypeName());
        }
        throw new RuntimeException("不支持的类型转换" + sourceType.getType().getName());
    }
}
