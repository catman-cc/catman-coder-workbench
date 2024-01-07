package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.ContainLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

public class ContainsLabelSelectorCreator extends AbstractLabelSelectorCreator{
    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        return ContainLabelSelector.builder()
                .value(jsonNode.get("value").asText())
                .build();
    }
}
