package cc.catman.workbench.api.server.configuration.page;

import cc.catman.workbench.service.core.common.page.EPageType;

import java.lang.annotation.*;

/**
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/5 13:59:58
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Page {
    /**
     * 忽略分页最大限制
     */
    boolean ignoreMaximumLimit() default false;

    /**
     * 默认分页大小限制,-1表示使用全局配置,如无需限制,请使用{@link #ignoreMaximumLimit()}
     */
    int maxPageSize() default -1;

    /**
     * 允许使用默认的分页参数
     */
    boolean allowDefaultPagingValue() default true;

    /**
     * 允许在滚动分页模式下,传入空ID,通常该场景应用在第一页
     */
    boolean allowScrollingPagingIDToBeNull() default true;

    /**
     * 默认支持的分页方式
     */
    EPageType supportPagingType() default EPageType.GENERAL;

    /**
     * type参数名称
     */
    String typeParameterName() default "";

    /**
     * 当前页参数名称
     */
    String pageParameterName() default "";

    /**
     * size参数名称
     */
    String sizeParameterName() default "";

    /**
     * id参数名称
     */
    String idParameterName() default "";

    /**
     * sort参数名称
     */
    String sortParameterName() default "";
}
