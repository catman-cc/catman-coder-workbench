package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.Constants;

/**
 * 函数类型,表示此处可以接受一个函数
 */
public class FunctionRawType extends DefaultType {

    public FunctionRawType() {
        this.typeName = Constants.Type.TYPE_NAME_FUNCTION;
    }
    @Override
    public boolean isFunction() {
        return true;
    }
}
