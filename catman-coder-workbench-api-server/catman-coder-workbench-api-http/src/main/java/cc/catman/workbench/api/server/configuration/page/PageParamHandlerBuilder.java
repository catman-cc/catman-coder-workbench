package cc.catman.workbench.api.server.configuration.page;

import  cc.catman.workbench.api.server.configuration.page.handler.PageParamHandler;
import  cc.catman.workbench.api.server.configuration.page.handler.auto.AutoPageParamHandler;
import  cc.catman.workbench.api.server.configuration.page.handler.general.GeneralPageParamHandler;
import  cc.catman.workbench.api.server.configuration.page.handler.scroll.ScrollPageParamHandler;
import cc.catman.workbench.service.core.common.page.EPageType;
import cc.catman.workbench.service.core.common.page.SortParam;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/6 9:34:14
 */
public class PageParamHandlerBuilder {
    /**
     * 类型参数名称
     */
    private String typeParameterName;
    /**
     * 当前页参数名称
     */
    private String pageParameterName;
    /**
     * 每页展示数量参数名称
     */
    private String sizeParameterName;
    /**
     * id参数名称
     */
    private String idParameterName;

    /**
     * 默认每页最大展示数量
     */
    private Integer maxPageSize;

    /**
     * 默认分页大小
     */
    private Integer defaultSize;

    /**
     * 默认当前页
     */
    private Integer defaultPage;

    /**
     * 是否为研发环境,该参数将控制部分校验行为
     */
    private boolean isDev;

    /**
     * 分页注解
     */
    private Page pageAnnotation;

    /**
     * 当前请求
     */
    private NativeWebRequest nativeWebRequest;

    /**
     * 传入的id属性
     */
    protected String id;
    /**
     * 传入的分页属性
     */
    protected Integer page;
    /**
     * 传入的size属性
     */
    protected Integer size;

    /**
     * 传入的排序属性
     */
    protected SortParam sortParam;

    public PageParamHandlerBuilder(String typeParameterName, String pageParameterName, String sizeParameterName, String idParameterName, Integer maxPageSize, Integer defaultSize, Integer defaultPage, boolean isDev, Page pageAnnotation, NativeWebRequest nativeWebRequest, String id, Integer page, Integer size,SortParam sortParam) {
        this.typeParameterName = typeParameterName;
        this.pageParameterName = pageParameterName;
        this.sizeParameterName = sizeParameterName;
        this.idParameterName = idParameterName;
        this.maxPageSize = maxPageSize;
        this.defaultSize = defaultSize;
        this.defaultPage = defaultPage;
        this.isDev = isDev;
        this.pageAnnotation = pageAnnotation;
        this.nativeWebRequest = nativeWebRequest;
        this.id = id;
        this.page = page;
        this.size = size;
        this.sortParam = sortParam;
    }

    public PageParamHandler builder(EPageType type) {
        switch (type) {
            case SCROLL: {
                return new ScrollPageParamHandler(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam);
            }
            case GENERAL: {
                return new GeneralPageParamHandler(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam);
            }
            default: {
                return new AutoPageParamHandler(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam);
            }
        }
    }
}
