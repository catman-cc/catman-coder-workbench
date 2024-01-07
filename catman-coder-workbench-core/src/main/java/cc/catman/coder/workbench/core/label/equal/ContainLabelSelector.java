package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class ContainLabelSelector extends AbstractLabelSelector<String> {
    @Override
    protected boolean doValid(Object object, ILabelSelectorContext context) {
         return unpackAndThen(object, value -> value.toString().contains(this.getValue()))
                 || asserArrayAndThen(object, array -> array.stream().anyMatch(item -> item.toString().equals(this.getValue())));
    }
}
