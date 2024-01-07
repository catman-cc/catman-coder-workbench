package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.Constants;

public class EnumType extends DefaultType{
    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_ENUM;
    }
}
