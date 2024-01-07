package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.ILabelSelectorContext;

public class NotExistsLabelSelector extends ExistsLabelSelector{
    @Override
    public boolean valid(Object labels, ILabelSelectorContext context) {
        return !super.valid(labels, context);
    }
}
