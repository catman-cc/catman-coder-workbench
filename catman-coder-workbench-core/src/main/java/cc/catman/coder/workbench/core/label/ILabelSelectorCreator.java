package cc.catman.coder.workbench.core.label;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface ILabelSelectorCreator {
    ILabelSelector<?> create(JsonNode jsonNode, ILabelSelectorFactory factory);
}
