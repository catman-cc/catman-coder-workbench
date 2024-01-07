package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.GteLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

public class GteLabelSelectorCreator extends AbstractLabelSelectorCreator{
    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        return GteLabelSelector.builder()
                .value(jsonNode.get("value").numberValue())
                .build();
    }
}
