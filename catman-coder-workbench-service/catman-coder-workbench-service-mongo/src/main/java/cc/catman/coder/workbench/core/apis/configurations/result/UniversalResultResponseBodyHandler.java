package cc.catman.coder.workbench.core.apis.configurations.result;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

public interface UniversalResultResponseBodyHandler {

    boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass);

    Object handler(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);

}
