package cc.catman.workbench.api.server.configuration.page.handler;

import cc.catman.workbench.service.core.common.page.PageParam;

/**
 * 加载PageParam对象
 *
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/6 10:11:29
 */
public interface PageParamHandler {

    PageParam resolveArgument();
}
