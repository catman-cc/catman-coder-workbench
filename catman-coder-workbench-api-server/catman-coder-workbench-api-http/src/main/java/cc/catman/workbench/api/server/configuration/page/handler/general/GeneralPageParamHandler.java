package cc.catman.workbench.api.server.configuration.page.handler.general;

import  cc.catman.workbench.api.server.configuration.exception.BusinessRuntimeException;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.GlobalECode;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.ValidECode;
import  cc.catman.workbench.api.server.configuration.page.Page;
import cc.catman.workbench.service.core.common.page.PageParam;
import  cc.catman.workbench.api.server.configuration.page.handler.BasePageParamHandler;
import cc.catman.workbench.service.core.common.page.SortParam;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 普通分页参数处理
 *
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/6 9:26:19
 */
public class GeneralPageParamHandler extends BasePageParamHandler {
    public GeneralPageParamHandler(String typeParameterName, String pageParameterName, String sizeParameterName, String idParameterName, Integer maxPageSize, Integer defaultSize, Integer defaultPage, boolean isDev, Page pageAnnotation, NativeWebRequest nativeWebRequest, String id, Integer page, Integer size, SortParam sortParam) {
        super(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam);
    }

    @Override
    public PageParam resolveArgument() {
        // 校验id参数
        if (isDev) {
            if (StringUtils.hasText(id)) {
                // 同时传入id和page.编码不规范,研发环境将触发异常
                throw BusinessRuntimeException.exception(GlobalECode.CODING_IS_NOT_STANDARDIZED, String.format("不允许同时传入[%s]和[%s]参数", idParameterName, typeParameterName));
            }
        }

        if (pageAnnotation.allowDefaultPagingValue()) {
            // 应用默认值
            if (page == null) {
                page = defaultPage;
            }
            if (size == null) {
                size = defaultSize;
            }
        }

        // 校验参数
        if (page == null) {
            throw BusinessRuntimeException.exception(ValidECode.NOT_NULL, pageParameterName);
        }
        if (size == null) {
            throw BusinessRuntimeException.exception(ValidECode.NOT_NULL, sizeParameterName);
        }

        checkPagingMaximum(size);

        return PageParam.of(page, size,sortParam);
    }
}
