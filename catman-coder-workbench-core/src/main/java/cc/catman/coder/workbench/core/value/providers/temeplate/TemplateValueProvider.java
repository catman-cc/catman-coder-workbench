package cc.catman.coder.workbench.core.value.providers.temeplate;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;

import java.util.Optional;

/**
 *  一个模版语言的处理器,可以使用模版语言来处理参数,模板语言的实现可以是freemarker,velocity,thymeleaf等.
 *  应用场景,比如:
 *  -  针对soap协议,动态拼接一下请求体
 *  -  可以动态拼接一下html,使其作为一个简单的SSR实现
 */
public class TemplateValueProvider extends AbstractValueProvider {

    @Override
    public Optional<Object> run(ValueProviderContext context) {
        return Optional.empty();
    }
}
