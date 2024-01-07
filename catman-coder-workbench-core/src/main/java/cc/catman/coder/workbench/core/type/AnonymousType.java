package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.type.complex.ComplexType;
import cc.catman.coder.workbench.core.type.complex.MapType;

/**
 * 匿名类型,该类型是一种十分特殊的类型,它本身没有任何意义,只是为其子元素提供一个容器,用于存放子元素
 * 因此在解析该类型的参数时,会忽略参数的取值器,也就说该类型的参数是不能设置取值器的.
 * 该类型在处理时,默认
 */
public class AnonymousType extends MapType {

    public AnonymousType() {
       this.typeName= Constants.Type.TYPE_NAME_ANONYMOUS;
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
