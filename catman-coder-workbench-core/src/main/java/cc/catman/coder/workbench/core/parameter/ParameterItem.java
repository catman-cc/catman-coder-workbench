package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.common.Scope;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterItem {
    private String name;

    private String itemId;
    /**
     * 子项的作用域,因为作用域可以由private转为public,理论上在更新一个类型的作用域时,应同步更新对应的TypeItem数据.
     * 所以,此处的作用域的值为private时,对应的实际类型定义的作用域可能是public
     */
    @Builder.Default
    private Scope itemScope=Scope.PRIVATE;
}
