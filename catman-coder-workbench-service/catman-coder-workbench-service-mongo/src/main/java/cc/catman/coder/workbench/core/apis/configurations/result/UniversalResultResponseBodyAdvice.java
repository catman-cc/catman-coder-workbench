package cc.catman.coder.workbench.core.apis.configurations.result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.annotation.Resource;

/**
 * 通用返回结果集的切面处理
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Import(UniversalResultResponseBodyConfigProperties.class)
public class UniversalResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private UniversalResultResponseBodyHandler universalResultResponseBodyHandler;


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return universalResultResponseBodyHandler.supports(methodParameter, aClass);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return universalResultResponseBodyHandler.handler(o, methodParameter, mediaType, aClass, serverHttpRequest, serverHttpResponse);
    }


}
