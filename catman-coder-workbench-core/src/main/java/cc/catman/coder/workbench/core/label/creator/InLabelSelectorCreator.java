package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.AbstractLabelSelector;
import cc.catman.coder.workbench.core.label.ILabelSelectorFactory;
import cc.catman.coder.workbench.core.label.equal.InLabelSelector;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class InLabelSelectorCreator extends AbstractLabelSelectorCreator{
    @Override
    protected AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory) {
        InLabelSelector inLabelSelector= InLabelSelector.builder().build();
        List<String> vs=new ArrayList<>();
        inLabelSelector.setValue(vs);
        JsonNode values = jsonNode.get("value");
        if (values.isArray()) {
            values.forEach(value -> vs.add(value.asText()));

        }
        return inLabelSelector;

    }
}
