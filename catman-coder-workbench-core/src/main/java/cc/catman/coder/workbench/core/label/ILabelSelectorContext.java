package cc.catman.coder.workbench.core.label;


public interface ILabelSelectorContext extends ISelectorContext<ILabelSelector<?>> {
    boolean valid(ILabelSelector<?> selector, Object object);
    boolean valid(String selectorJson, Object object);
}
