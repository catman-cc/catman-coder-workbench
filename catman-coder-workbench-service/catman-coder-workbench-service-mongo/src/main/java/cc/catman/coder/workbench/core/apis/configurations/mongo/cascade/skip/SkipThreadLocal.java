package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.skip;

import java.util.Optional;

public class SkipThreadLocal {
    public static ThreadLocal<Boolean> SKIP=ThreadLocal.withInitial(()-> false);

    public static void clear(){
        SKIP.remove();
    }

    public static void set(boolean v){
        SKIP.set(v);
    }

    public static boolean skip(){
        return Optional.ofNullable(SKIP.get()).orElse(false);
    }
}
