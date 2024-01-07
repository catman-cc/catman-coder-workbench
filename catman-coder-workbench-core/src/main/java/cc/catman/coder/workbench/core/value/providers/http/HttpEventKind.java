package cc.catman.coder.workbench.core.value.providers.http;

/**
 * Http事件类型,在执行过程中会进行三个特定的事件通知
 * - BEFORE_HTTP_REQUEST: 在执行Http请求之前,构建请求参数之后执行,会上报请求参数信息
 * - HTTP_REQUEST_DONE: Http请求执行完成,会上报请求结果信息,包括请求和响应的完整数据
 * - HTTP_REQUEST_FAILED: Http请求执行失败,会上报请求失败的信息,包括完整的异常信息
 */
public enum HttpEventKind {
    BEFORE_HTTP_REQUEST,
    HTTP_REQUEST_DONE,
    HTTP_REQUEST_FAILED,
}
