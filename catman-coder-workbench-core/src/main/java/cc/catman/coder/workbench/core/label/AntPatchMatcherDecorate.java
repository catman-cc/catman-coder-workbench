package cc.catman.coder.workbench.core.label;


import org.springframework.util.AntPathMatcher;

public class AntPatchMatcherDecorate implements IMatcher{
    private final String pattern;

    private  final AntPathMatcher antPathMatcher;

    public AntPatchMatcherDecorate(String pattern) {
        this.pattern=pattern;
        this.antPathMatcher = new AntPathMatcher();
    }
    public static AntPatchMatcherDecorate of(String pattern) {
       return new AntPatchMatcherDecorate(pattern);
    }



    @Override
    public boolean match(String value) {
        return this.antPathMatcher.matchStart(this.pattern, value);
    }
}
