package cc.catman.coder.workbench.core.label;

import com.fasterxml.jackson.databind.JsonNode;

public interface ILabelSelectorFactory {
    ILabelSelector<?> create(JsonNode node);
}
