package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.ExistsLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

public class ExistsLabelSelectorCreator extends AbstractLabelSelectorCreator{

    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        return ExistsLabelSelector.builder().build();
    }
}
