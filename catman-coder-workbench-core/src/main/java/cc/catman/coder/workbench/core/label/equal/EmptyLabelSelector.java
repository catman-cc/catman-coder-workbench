package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Map;

@SuperBuilder
@AllArgsConstructor
public class EmptyLabelSelector extends AbstractLabelSelector<String> {
    public boolean doValid(Object object, ILabelSelectorContext context) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return ((String) object).isEmpty();
        }
        if (object instanceof Collection<?>) {
            return ((Collection<?>) object).isEmpty();
        }
        if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        }
        if (object instanceof Map<?,?>) {
            return ((Map<?,?>) object).isEmpty();
        }
        return false;
    }

}
