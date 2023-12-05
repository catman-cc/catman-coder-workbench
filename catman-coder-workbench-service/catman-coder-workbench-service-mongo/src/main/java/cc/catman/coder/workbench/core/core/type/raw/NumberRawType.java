package cc.catman.coder.workbench.core.core.type.raw;

import cc.catman.coder.workbench.core.core.Constants;
import cc.catman.coder.workbench.core.core.entity.NumberEntity;
import cc.catman.coder.workbench.core.core.type.Type;

import java.math.BigDecimal;
import java.util.Optional;

public class NumberRawType extends RawType<Number> {
    public NumberRawType() {
        super(Constants.DefaultValue.NUMBER.get());
    }

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_NUMBER;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return targetType instanceof NumberRawType;
    }

    @Override
    public NumberEntity toEntity(Object obj) {
        return Optional.ofNullable(obj)
                .map(o -> {
                    if (o instanceof Number n) {
                        return new NumberEntity(n);
                    } else if (o instanceof Boolean b) {
                        return new NumberEntity(b ? 1 : 0);
                    } else if (o instanceof String s) {
                        return new NumberEntity(new BigDecimal(s));
                    }
                    return new NumberEntity();
                }).orElseGet(NumberEntity::new);
    }
}
