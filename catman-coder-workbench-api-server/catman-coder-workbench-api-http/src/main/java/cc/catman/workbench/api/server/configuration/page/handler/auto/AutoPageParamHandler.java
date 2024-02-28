package cc.catman.workbench.api.server.configuration.page.handler.auto;

import  cc.catman.workbench.api.server.configuration.exception.BusinessRuntimeException;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.GlobalECode;
import  cc.catman.workbench.api.server.configuration.page.Page;
import cc.catman.workbench.service.core.common.page.PageParam;
import  cc.catman.workbench.api.server.configuration.page.handler.BasePageParamHandler;
import  cc.catman.workbench.api.server.configuration.page.handler.general.GeneralPageParamHandler;
import  cc.catman.workbench.api.server.configuration.page.handler.scroll.ScrollPageParamHandler;
import cc.catman.workbench.service.core.common.page.SortParam;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/6 9:34:14
 */
public class AutoPageParamHandler extends BasePageParamHandler {

    private BasePageParamHandler delegate;


    public AutoPageParamHandler(String typeParameterName, String pageParameterName, String sizeParameterName, String idParameterName, Integer maxPageSize, Integer defaultSize, Integer defaultPage, boolean isDev, Page pageAnnotation, NativeWebRequest nativeWebRequest, String id, Integer page, Integer size, SortParam sortParam) {
        super(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam);
        constructionVerification();

    }

    private void constructionVerification() {
        if (page == null && id == null) {
            // 无法校验,抛异常
            throw BusinessRuntimeException.exception(GlobalECode.MISSING_PARAMETER, pageParameterName.concat(" | ").concat(idParameterName));
        }
        delegate = page == null
                ? new ScrollPageParamHandler(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam)
                : new GeneralPageParamHandler(typeParameterName, pageParameterName, sizeParameterName, idParameterName, maxPageSize, defaultSize, defaultPage, isDev, pageAnnotation, nativeWebRequest, id, page, size,sortParam);
    }

    @Override
    public PageParam resolveArgument() {
        return delegate.resolveArgument();
    }
}
