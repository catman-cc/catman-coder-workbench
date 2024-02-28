package cc.catman.workbench.api.server.configuration.page.handler;

import  cc.catman.workbench.api.server.configuration.exception.BusinessRuntimeException;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.GlobalECode;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.ValidECode;
import  cc.catman.workbench.api.server.configuration.page.Page;
import cc.catman.workbench.service.core.common.page.SortParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/6 9:09:28
 */
@Slf4j
public abstract class BasePageParamHandler implements PageParamHandler {
    /**
     * 类型参数名称
     */
    protected String typeParameterName;
    /**
     * 当前页参数名称
     */
    protected String pageParameterName;
    /**
     * 每页展示数量参数名称
     */
    protected String sizeParameterName;
    /**
     * id参数名称
     */
    protected String idParameterName;

    /**
     * 默认每页最大展示数量
     */
    protected Integer maxPageSize;

    /**
     * 默认分页大小
     */
    protected Integer defaultSize;

    /**
     * 默认当前页
     */
    protected Integer defaultPage;

    /**
     * 是否为研发环境,该参数将控制部分校验行为
     */
    protected boolean isDev;

    /**
     * 分页注解
     */
    protected Page pageAnnotation;

    /**
     * 当前请求
     */
    protected NativeWebRequest nativeWebRequest;



    public BasePageParamHandler(String typeParameterName, String pageParameterName, String sizeParameterName, String idParameterName, Integer maxPageSize, Integer defaultSize, Integer defaultPage, boolean isDev, Page pageAnnotation, NativeWebRequest nativeWebRequest, String id, Integer page, Integer size,SortParam sortParam) {
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
     * 排序参数
     */
    protected SortParam sortParam;

    /**
     * 字符串转数字
     *
     * @param number    数字模式串
     * @param paramName 参数名称
     * @return 数字
     */
    protected Integer strToInteger(String number, String paramName) {
        if (number == null) {
            return null;
        }
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            // 转换为自定义异常
            throw BusinessRuntimeException.exception(ValidECode.DIGITS, paramName);
        }
    }

    /**
     * 校验分页最大会
     *
     * @param size 分页值
     */
    protected void checkPagingMaximum(Integer size) {
        if (pageAnnotation.ignoreMaximumLimit()) {
            // 忽略分页最大限制
            return;
        }
        if (size > maxPageSize) {
            if (isDev) {
                throw BusinessRuntimeException.exception(GlobalECode.CODING_IS_NOT_STANDARDIZED, String.format("当前接口限制分页大小为[%d],调用值为[%d],如需突破限制,请联系开发人员!", maxPageSize, size));
            }
            log.warn("当前接口限制分页大小为[{}],调用值为[{}],如需突破限制,请联系开发人员!", maxPageSize, size);
        }
    }
}
