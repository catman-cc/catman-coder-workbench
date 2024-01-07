package cc.catman.coder.workbench.core.label.equal;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class GtLabelSelector extends AbstractNumberLabelSelector {

    @Override
    protected boolean compare(Number number1, Number number2) {
        return number2.doubleValue() > number1.doubleValue();
    }
}
