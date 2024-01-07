package cc.catman.coder.workbench.core.label;

public interface ISelectorContext<S> {
    boolean valid(S selector, Object object);
    boolean valid(String selectorJson, Object object);
}
