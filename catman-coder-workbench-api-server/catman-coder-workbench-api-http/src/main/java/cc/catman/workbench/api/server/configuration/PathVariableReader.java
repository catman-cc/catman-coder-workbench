package cc.catman.workbench.api.server.configuration;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

public final class PathVariableReader {
    private static AntPathMatcher antPathMatcher=new AntPathMatcher();

    public static PathVariables read(String pattern,String path){
        return new PathVariables(antPathMatcher.extractUriTemplateVariables(pattern, path));
    }

    public static class PathVariables{
        private Map<String,String> pathVariables;

        public PathVariables(Map<String, String> pathVariables) {
            this.pathVariables = pathVariables;
        }

        public String getOrDefault(String key, String defaultValue){
            return pathVariables.getOrDefault(key,defaultValue);
        }
    }
}
