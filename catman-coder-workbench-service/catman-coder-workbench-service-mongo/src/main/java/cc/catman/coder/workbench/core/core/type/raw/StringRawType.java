package cc.catman.coder.workbench.core.core.type.raw;

import cc.catman.coder.workbench.core.core.Constants;
import cc.catman.coder.workbench.core.core.entity.StringEntity;
import cc.catman.coder.workbench.core.core.type.Type;

import java.util.Optional;

public class StringRawType extends RawType<String> {
    public StringRawType() {
        super(Constants.DefaultValue.STRING.get());
    }

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_STRING;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return targetType instanceof StringRawType;
    }

    @Override
    public StringEntity toEntity(Object obj) {
      return new StringEntity(Optional.ofNullable(obj).map(o-> o instanceof String s?s:String.valueOf(o)).orElse(null));
    }
}
