package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class NotInLabelSelector extends InLabelSelector {
    @Override
    public boolean valid(Object labels, ILabelSelectorContext context) {
        return !super.valid(labels, context);
    }
}
