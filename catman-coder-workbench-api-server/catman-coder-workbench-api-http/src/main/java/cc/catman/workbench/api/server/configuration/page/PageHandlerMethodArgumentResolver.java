package cc.catman.workbench.api.server.configuration.page;

import  cc.catman.workbench.api.server.configuration.exception.BusinessRuntimeException;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.GlobalECode;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.ValidECode;
import cc.catman.workbench.service.core.common.page.EPageType;
import cc.catman.workbench.service.core.common.page.PageParam;
import cc.catman.workbench.service.core.common.page.SortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/5 13:59:36
 */
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    // 默认值

    public static final String DEFAULT_TYPE_PARAMETER = "type";
    public static final String DEFAULT_PAGE_PARAMETER = "current";
    public static final String DEFAULT_SIZE_PARAMETER = "pageSize";
    public static final String DEFAULT_ID_PARAMETER = "lastId";
    public static final String DEFAULT_SORTS_PARAMETER= "sorts";

    /**
     * 类型参数名称
     */
    private String typeParameterName = DEFAULT_TYPE_PARAMETER;
    /**
     * 当前页参数名称
     */
    private String pageParameterName = DEFAULT_PAGE_PARAMETER;
    /**
     * 每页展示数量参数名称
     */
    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;
    /**
     * id参数名称
     */
    private String idParameterName = DEFAULT_ID_PARAMETER;

    private String sortParameterName = DEFAULT_SORTS_PARAMETER;

    /**
     * 默认每页最大展示数量
     */
    private Integer maxPageSize = 2000;

    /**
     * 默认分页大小
     */
    private Integer defaultSize = 10;

    /**
     * 默认当前页
     */
    private Integer defaultPage = 1;

    /**
     * 开发模式才会触发编码不规范导致的异常问题
     */
    private boolean idDev = true;


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 限制注解和类型

        // step1: 校验参数类型
        if (PageParam.class.equals(methodParameter.getParameterType())) {

            if (methodParameter.hasParameterAnnotation(Page.class)) {
                return true;
            }
            // 如果开发者未指定Page注解,记录warn日志
            if (log.isWarnEnabled()) {

                log.warn("参数[{}]类型为:{},但因未配置{}注解,将无法完成设值操作."
                        , methodParameter.getParameterName()
                        , methodParameter.getParameterType()
                        , Page.class
                );
            }
        }

        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter
            , ModelAndViewContainer modelAndViewContainer
            , NativeWebRequest nativeWebRequest
            , WebDataBinderFactory webDataBinderFactory
    ) throws Exception {
        // 加载Page注解
        Page pageAnnotation = methodParameter.getParameterAnnotation(Page.class);

        if (pageAnnotation == null) {
            // never exec ...
            return null;
        }

        // 确定参数名称
        String typeParamName = StringUtils.hasText(pageAnnotation.typeParameterName()) ? pageAnnotation.typeParameterName() : typeParameterName;
        String idParamName = StringUtils.hasText(pageAnnotation.idParameterName()) ? pageAnnotation.idParameterName() : idParameterName;
        String pageParamName = StringUtils.hasText(pageAnnotation.pageParameterName()) ? pageAnnotation.pageParameterName() : pageParameterName;
        String sizeParamName = StringUtils.hasText(pageAnnotation.sizeParameterName()) ? pageAnnotation.sizeParameterName() : sizeParameterName;
        String sortParamName = StringUtils.hasText(pageAnnotation.sortParameterName()) ? pageAnnotation.sortParameterName() : sortParameterName;
        // 加载类型数据
        String typeName = Optional.ofNullable(nativeWebRequest.getParameter(typeParamName)).map(s -> s.toUpperCase().trim()).orElse(EPageType.AUTO.name());

        EPageType currentType = conversionType(typeName);
        // 类型校验
        if (typeNotSupported(Objects.requireNonNull(pageAnnotation).supportPagingType(), currentType)) {
            // 代码调用不规范
            if (idDev) {
                // 研发环境,直接抛出异常,提醒修改方式
                throw BusinessRuntimeException.exception(GlobalECode.CODING_IS_NOT_STANDARDIZED
                        , String.format("请求方式[%s]不被支持,请使用[%s]模式下对应的方式", currentType, pageAnnotation.supportPagingType())
                );

            }
            if (log.isWarnEnabled()) {
                log.warn("不受支持的分页类型:[{}],当前方法仅支持:[{}]", currentType, pageAnnotation.supportPagingType());
            }
            return null;
        }
        // 加载参数
        String id = nativeWebRequest.getParameter(idParamName);
        Integer page = strToInteger(nativeWebRequest.getParameter(pageParamName), pageParamName);
//        // 为了适配ant design ,这里需要将page的值-1, 前端处理
//        if(page!=null){
//            page=page-1;
//        }
        Integer size = strToInteger(nativeWebRequest.getParameter(sizeParamName), sizeParamName);

        // 根据类型校验参数
        String[] sorts=nativeWebRequest.getParameterValues(sortParamName);
        SortParam sortParam=resolveSortParam(sorts);


        return new PageParamHandlerBuilder(
                typeParamName, pageParamName, sizeParamName, idParamName
                , maxPageSize, defaultSize, defaultPage, idDev, pageAnnotation, nativeWebRequest
                , id, page, size,sortParam
        ).builder(currentType).resolveArgument();
    }

    private EPageType conversionType(String name) {
        try {
            return EPageType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw BusinessRuntimeException.exception(GlobalECode.INVALID_PARAMETER, name, "请选择正确的分页方式:".concat(Arrays.toString(EPageType.values())));
        }
    }


    private boolean typeNotSupported(EPageType supports, EPageType current) {
        if (supports == null) {
            return false;
        }
        if (supports.equals(EPageType.AUTO)) {
            return true;
        }
        return supports.equals(current);
    }

    private SortParam resolveSortParam(String[] sorts){
        if(sorts==null||sorts.length==0){
            return null;
        }
        SortParam sortParam=SortParam.empty();
        List<String> sortList=Arrays.asList(sorts);
        sortList.forEach(sort->{
            if(!sort.contains(":")){
                throw BusinessRuntimeException.exception(ValidECode.INVALID_PARAMETER_FORMAT,sort,"排序参数格式错误,正确格式为:字段名:排序方式,如:age:desc");
            }
            String[] sortArray=sort.split(":");
            if(sortArray.length!=2){
                throw BusinessRuntimeException.exception(ValidECode.INVALID_PARAMETER_FORMAT,sort,"排序参数格式错误,正确格式为:字段名:排序方式,如:age:desc");
            }
            sortParam.addOrder(sortArray[0],sortArray[1]);
        });
        return sortParam;
    }

    public Integer strToInteger(String s, String name) {
        if (s == null) {
            return null;
        }
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            // 转换为自定义异常
            throw BusinessRuntimeException.exception(ValidECode.DIGITS, name);
        }

    }
}
