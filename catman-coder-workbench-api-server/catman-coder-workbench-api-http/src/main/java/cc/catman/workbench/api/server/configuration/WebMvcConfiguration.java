package cc.catman.workbench.api.server.configuration;

import cc.catman.workbench.api.server.configuration.page.PageHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 当前用户数据参数解析器
        resolvers.add(new PageHandlerMethodArgumentResolver());
    }
}