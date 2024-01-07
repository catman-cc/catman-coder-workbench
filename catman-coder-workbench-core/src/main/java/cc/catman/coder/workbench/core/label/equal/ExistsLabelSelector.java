package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class ExistsLabelSelector extends AbstractLabelSelector<Object> {

    @Override
    protected boolean doValid(Object object, ILabelSelectorContext context) {
       return object!=null;
    }
}
