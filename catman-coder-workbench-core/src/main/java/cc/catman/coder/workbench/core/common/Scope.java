package cc.catman.coder.workbench.core.common;

import cc.catman.coder.workbench.core.Base;

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

    public static boolean isPublic(Scope scope) {
        return Scope.PUBLIC.equals(scope);
    }

    public static boolean isPublic(Base base){
        return base != null && isPublic(base.getScope());
    }
}
