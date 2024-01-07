package cc.catman.coder.workbench.core.label;

import java.util.List;

public interface ILabelSelector<T> extends ISelector<ILabelSelector<?>,ILabelSelectorContext>{
    String getMatch();

    void setMatch(String match);

    String getKind();

    T getValue();

    List<ILabelSelector<?>> getRules();

    ILabelSelector<T> addRule(ILabelSelector<?> rule);

    boolean match(String name, ILabelSelectorContext context);

    boolean valid(Object labels, ILabelSelectorContext context);

}
