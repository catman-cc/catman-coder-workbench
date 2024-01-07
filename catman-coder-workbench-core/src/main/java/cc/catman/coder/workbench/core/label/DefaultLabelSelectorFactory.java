package cc.catman.coder.workbench.core.label;

import cc.catman.coder.workbench.core.label.creator.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultLabelSelectorFactory implements ILabelSelectorFactory{
    public static DefaultLabelSelectorFactory create(){
        return new DefaultLabelSelectorFactory();
    }

    public static DefaultLabelSelectorFactory createDefault(){
        return DefaultLabelSelectorFactory.create()
                .add("All",new AllLabelSelectorCreator())
                .add("Any",new AnyLabelSelectorCreator())
                .add("None",new NoneLabelSelectorCreator())

                .add("StartWith",new StartWithLabelSelectorCreator())
                .add("EndWith",new EndWithLabelSelectorCreator())
                .add("Regex",new RegexLabelSelectorCreator())

                .add("Gt",new GtLabelSelectorCreator())
                .add("Lt",new LtLabelSelectorCreator())
                .add("Gte",new GteLabelSelectorCreator())
                .add("Lte",new LteLabelSelectorCreator())

                .add("Equals",new EqualLabelSelectorCreator())
                .add("NotEquals",new NotEqualLabelSelectorCreator())

                .add("NotEmpty",new NotEmptyLabelSelectorCreator())
                .add("Empty",new EmptyLabelSelectorCreator())
                .add("Exists",new ExistsLabelSelectorCreator())
                .add("NotExist",new NotExistsLabelSelectorCreator())
                .add("Contains",new ContainsLabelSelectorCreator())
                .add("In",new InLabelSelectorCreator())
                .add("NotIn",new NotInLabelSelectorCreator())
                ;
    }
    @Builder.Default
    private Map<String, ILabelSelectorCreator> labelSelectorCreatorMap=new HashMap<>();

    public DefaultLabelSelectorFactory add(String kind, ILabelSelectorCreator creator) {
        labelSelectorCreatorMap.put(kind, creator);
        return this;
    }

    @Override
    public ILabelSelector<?> create(JsonNode node) {
        String kind = node.get("kind").asText();
        ILabelSelectorCreator creator = labelSelectorCreatorMap.get(kind);
        if (creator != null) {
            ILabelSelector<?> selector = creator.create(node, this);
            Optional.ofNullable(selector).ifPresent(s->{
               recursionMatch(s,s.getMatch());
            });
            return  selector;
        }
        throw new RuntimeException("not support kind: " + kind+" ,original json:"+node.toString());
    }

    void recursionMatch(ILabelSelector<?> selector,String match){
        if (selector.getRules().isEmpty()||Optional.ofNullable(match).isEmpty()){
            return;
        }
        String matchValue=Optional.ofNullable(selector.getMatch()).orElse(match);
        for (ILabelSelector<?> rule : selector.getRules()) {
            if (Optional.ofNullable(rule.getMatch()).isEmpty()){
                rule.setMatch(matchValue);
            }
            if (rule instanceof AbstractLabelSelector){
                recursionMatch(rule,matchValue);
            }
        }
    }
}
