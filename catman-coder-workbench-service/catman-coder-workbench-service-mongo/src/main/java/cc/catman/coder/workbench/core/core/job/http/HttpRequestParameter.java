package cc.catman.coder.workbench.core.core.job.http;

import java.util.Map;

import cc.catman.coder.workbench.core.core.type.IsType;

@IsType
public class HttpRequestParameter {
    /**
     * 环境变量信息
     */
    private Map<String, String> environmentVariables;

    private String url;

}
