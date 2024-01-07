package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.NotInLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

public class NotInLabelSelectorCreator extends AbstractLabelSelectorCreator{
    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        return NotInLabelSelector.builder().build();
    }
}
