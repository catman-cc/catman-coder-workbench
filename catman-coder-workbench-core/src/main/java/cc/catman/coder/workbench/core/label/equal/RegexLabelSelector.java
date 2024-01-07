package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class RegexLabelSelector extends AbstractLabelSelector<String> {
    @Override
    protected boolean doValid(Object object, ILabelSelectorContext context) {
        return unpackAndThen(object, value -> value.toString().matches(this.getValue()));
    }
}
