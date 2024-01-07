package cc.catman.workbench.api.server.websocket.run.value;

import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.workbench.api.server.websocket.run.IDebugContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebuggableParameterParseHandlerContext implements IParameterParseHandlerContext {

    private IParameterParseHandlerContext proxy;

    private IDebugContext debugContext;

    @Override
    public Map<String, Object> buildVariables() {
        return proxy.buildVariables();
    }

    @Override
    public ValueProviderContext getValueProviderContext() {
        return proxy.getValueProviderContext();
    }

    @Override
    public IParameterParseHandlerContext registerParameterParseStrategy(IParameterParseStrategy strategy) {
        return proxy.registerParameterParseStrategy(strategy);
    }

    @Override
    public <T> T parse(Parameter parameter, TypeDescriptor descriptor) {
        return proxy.parse(parameter, descriptor);
    }
}
