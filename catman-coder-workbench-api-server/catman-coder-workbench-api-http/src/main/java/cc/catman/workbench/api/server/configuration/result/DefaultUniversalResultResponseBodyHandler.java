package cc.catman.workbench.api.server.configuration.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class DefaultUniversalResultResponseBodyHandler implements UniversalResultResponseBodyHandler, InitializingBean {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    protected UniversalResultResponseBodyConfigProperties configProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Class<?> clazz = methodParameter.getDeclaringClass();

        if (filterExcludes(clazz.getCanonicalName())) {
            // 手动排除的包,通过注解可以强制开启处理
            boolean shouldHandler = clazz.isAnnotationPresent(ForceResult.class)
                    || methodParameter.getAnnotatedElement().isAnnotationPresent(ForceResult.class);

            if (shouldHandler && log.isTraceEnabled()) {
                log.trace("{}#{} by marking the {} annotation, so it will be handler."
                        , aClass.getSimpleName()
                        , Objects.requireNonNull(methodParameter.getMethod()).getName()
                        , ForceResult.class.getSimpleName());
            }
            return clazz.isAnnotationPresent(ForceResult.class) || methodParameter.getAnnotatedElement().isAnnotationPresent(ForceResult.class);
        }

        if (filterIncludes(clazz.getCanonicalName())) {
            // 手动包含的包，通过注解可以强制排除
            // 提供注解，跳出统一切面处理
            boolean shouldHandler = !clazz.isAnnotationPresent(IgnoreResult.class) && !methodParameter.getAnnotatedElement().isAnnotationPresent(IgnoreResult.class);

            if (!shouldHandler && log.isTraceEnabled()) {
                log.trace("{}#{} by marking the {} annotation, so it`s ignored.", aClass.getSimpleName(), Objects.requireNonNull(methodParameter.getMethod()).getName(), IgnoreResult.class.getSimpleName());
            }
            return shouldHandler;

        }
        // 未显示指定需要包含的包，只处理强制注解
        return configProperties.isProcessByDefault()||clazz.isAnnotationPresent(ForceResult.class)
                || methodParameter.getAnnotatedElement().isAnnotationPresent(ForceResult.class);
    }

    @Override
    public Object handler(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof Result) {
            Class<?> clazz = methodParameter.getDeclaringClass();

            if (!clazz.isAnnotationPresent(ForceResult.class) && !methodParameter.getAnnotatedElement().isAnnotationPresent(ForceResult.class)) {
                return o;
            }

            if (log.isTraceEnabled()) {
                log.trace("The current returned object is an instance of {}, but is annotated with {}  on the method or class,so we will wrap it further,object info:{}", Result.class, ForceResult.class, o);
            }
        }
        return wrapper(o);
    }

    private Object wrapper(Object obj) {
        Result<Object> rs = UniversalResultHelper.success(obj);
        // 兼容字符串
        return obj instanceof String ? write2String(rs) : rs;

    }

    private String write2String(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (objectMapper == null) {
            if (log.isWarnEnabled()) {
                log.warn("Cannot get an instance of ObjectMapper  from Spring container,So a normal instance of ObjectMapper is manually initialized.");
            }
            objectMapper = new ObjectMapper();
        }
    }

    protected boolean filterExcludes(String clazz) {
        return anyMatch(configProperties.getExcludesPackages(), clazz);
    }


    protected boolean filterIncludes(String clazz) {
        return anyMatch(configProperties.getIncludePackages(), clazz);
    }

    private boolean anyMatch(List<String> regex, String clazz) {
        if (CollectionUtils.isEmpty(regex)) {
            return false;
        }
        return regex.stream().anyMatch((r) -> match(r, clazz));
    }

    private boolean match(String regex, String clazz) {
        return clazz.matches(regex);
    }
}
