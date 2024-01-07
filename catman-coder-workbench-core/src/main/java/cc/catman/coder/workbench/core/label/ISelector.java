package cc.catman.coder.workbench.core.label;

public interface ISelector<S,C extends ISelectorContext<S>> {
    boolean valid(Object labels, C context);
}
