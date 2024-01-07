package cc.catman.coder.workbench.core.trigger.http;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.job.JobDefinition;

import java.util.List;

/**
 * Http触发器配置
 */
public class HttpTriggerInfo extends Base {
    private String id;
    private String name;
    /**
     * 请求路径,支持通配符,比如 /api/v1/*
     */
    private String urlPattern;

    /**
     * 支持的Http方法,比如 GET,POST,PUT,DELETE
     */
    private List<String> supportedMethods;

    /**
     * 触发器对应的任务定义
     */
    private JobDefinition jobDefinition;
}
