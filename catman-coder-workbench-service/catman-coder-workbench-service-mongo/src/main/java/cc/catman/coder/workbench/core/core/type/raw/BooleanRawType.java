package cc.catman.coder.workbench.core.core.type.raw;

import cc.catman.coder.workbench.core.core.Constants;
import cc.catman.coder.workbench.core.core.entity.BooleanEntity;
import cc.catman.coder.workbench.core.core.type.Type;

import java.util.Optional;

public class BooleanRawType extends RawType<Boolean> {
    public BooleanRawType() {
        super(Constants.DefaultValue.BOOLEAN.get());
    }

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_BOOLEAN;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return targetType instanceof BooleanRawType;
    }

    @Override
    public BooleanEntity toEntity(Object obj) {
        return new BooleanEntity(Optional.ofNullable(obj).map(o->{
                    if ( o instanceof Boolean b){
                        return b;
                    }else if (o instanceof String s){
                        String lowerS=s.trim().toLowerCase();
                        return lowerS.equals("t")||lowerS.equals("true")||lowerS.equals("1");
                    }else if (o instanceof Number n){
                        return n.equals(1);
                    }
                    return false;
                }
                ).orElse(null));
    }
}
