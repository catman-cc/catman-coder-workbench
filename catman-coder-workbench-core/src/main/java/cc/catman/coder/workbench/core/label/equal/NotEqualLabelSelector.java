package cc.catman.coder.workbench.core.label.equal;

import cc.catman.coder.workbench.core.label.ILabelSelectorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class NotEqualLabelSelector extends EqualsLabelSelector{
        @Override
        public boolean valid(Object labels, ILabelSelectorContext context) {
            return !super.valid(labels, context);
        }
}
