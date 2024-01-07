package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.EqualsLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

public class EqualLabelSelectorCreator extends AbstractLabelSelectorCreator{
    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        return EqualsLabelSelector.builder()
                .value(jsonNode.get("value").asText())
                .build();
    }
}
