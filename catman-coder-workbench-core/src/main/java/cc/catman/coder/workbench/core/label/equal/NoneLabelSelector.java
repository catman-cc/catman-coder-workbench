package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class NoneLabelSelector extends AbstractLabelSelector<Object> {
    @Override
    public boolean valid(Object labels, ILabelSelectorContext context) {
        if(this.rules.isEmpty()){
            return false;
        }
        return this.rules.stream().noneMatch(rule -> rule.valid(labels, context));
    }
}
