package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public abstract class AbstractNumberLabelSelector extends AbstractLabelSelector<Number>{
    @Override
    protected boolean doValid(Object object, ILabelSelectorContext context) {
        return unpackAndThen(object, obj-> assertToNumberAndThen(obj, number-> compare(this.getValue(), number)));
    }

    protected abstract boolean compare(Number number1, Number number2);
}
