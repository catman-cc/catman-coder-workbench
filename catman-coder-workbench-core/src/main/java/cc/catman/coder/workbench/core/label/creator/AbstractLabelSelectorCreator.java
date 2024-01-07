package cc.catman.coder.workbench.core.label.creator;

import cc.catman.coder.workbench.core.label.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public abstract class AbstractLabelSelectorCreator implements ILabelSelectorCreator {
    @Override
    public ILabelSelector<?> create(JsonNode jsonNode, ILabelSelectorFactory factory) {
        AbstractLabelSelector<?> selector=doCreate(jsonNode,factory);
        if (selector!=null){
            selector.setKind(jsonNode.get("kind").asText());
            Optional.ofNullable(jsonNode.get("match")).ifPresent(match->selector.setMatch(match.asText()));
            selector.setDefinition(jsonNode);
            selector.setMatcher(new AntPatchMatcherDecorate(selector.getMatch()));
            if (jsonNode.has("rules")){
                JsonNode rules = jsonNode.get("rules");
                if (!rules.isArray()){
                    throw new IllegalArgumentException("rules must be array:"+jsonNode.toString());
                }
                for (JsonNode rule : rules) {
                    ILabelSelector<?> ruleSelector = factory.create(rule);
                    selector.getRules().add(ruleSelector);
                }
            }

        }
        // 递归处理match值,主要是复合selector其子元素的match值
        return selector;
    }



    /**
     * 实现类需要返回一个填充了value和rule的对象
     * @param jsonNode jsonNode
     * @param factory factory
     */
    protected abstract AbstractLabelSelector<?> doCreate(JsonNode jsonNode, ILabelSelectorFactory factory);
}
