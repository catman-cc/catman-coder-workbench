package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 在给出的值中,匹配任意一个
 * 值得注意的是,labels的类型是List<String>,所以,这里需要断言一下值必须不是集合类型
 */
@SuperBuilder
@AllArgsConstructor
public class InLabelSelector extends AbstractLabelSelector<List<String>> {

    @Override
    protected boolean doValid(Object object, ILabelSelectorContext context) {
        return unpackAndThen(object, obj-> this.getValue().contains(object.toString()));
    }
}
