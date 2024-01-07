package cc.catman.coder.workbench.core.common;

/**
 * 可用范围
 */
public enum Scope {
    PUBLIC,
    PRIVATE,
;
    public boolean isPublic() {
        return this == Scope.PUBLIC;
    }

    public static boolean isPublic(String scope) {
        return Scope.PUBLIC.name().equalsIgnoreCase(scope);
    }
}
