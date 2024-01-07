package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class EqualsLabelSelector extends AbstractLabelSelector<String> {

    @Override
    public boolean doValid(Object object, ILabelSelectorContext context) {
        return unpackAndThen(object, value -> this.getValue().equals(value.toString()));
    }

}
