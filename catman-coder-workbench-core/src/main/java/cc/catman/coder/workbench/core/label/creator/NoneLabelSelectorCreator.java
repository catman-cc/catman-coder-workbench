package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.NoneLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

public class NoneLabelSelectorCreator extends AbstractLabelSelectorCreator{
    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        return NoneLabelSelector.builder().build();
    }
}
