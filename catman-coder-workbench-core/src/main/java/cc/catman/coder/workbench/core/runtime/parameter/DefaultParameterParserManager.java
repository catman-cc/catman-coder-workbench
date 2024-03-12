package cc.catman.coder.workbench.core.runtime.parameter;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IParameterParserManager;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;
import cc.catman.coder.workbench.core.runtime.ParameterParserResult;
import cc.catman.coder.workbench.core.runtime.parameter.parser.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的参数解析器管理
 */
public class DefaultParameterParserManager implements IParameterParserManager {

    private final List<IParameterParseStrategy> strategies;

    public DefaultParameterParserManager() {
        this.strategies=new ArrayList<>();
        this.strategies.add(new RawParameterParseStrategy(String.class, Constants.Type.TYPE_NAME_STRING));
        this.strategies.add(new RawParameterParseStrategy(Double.class, Constants.Type.TYPE_NAME_NUMBER));
        this.strategies.add(new RawParameterParseStrategy(Boolean.class, Constants.Type.TYPE_NAME_BOOLEAN));
        this.strategies.add(new AnonymousParameterParseStrategy());
        this.strategies.add(new ArrayParameterParseStrategy());
        this.strategies.add(new FileParameterParseStrategy());
        this.strategies.add(new FunctionCallInfoParameterParseStrategy());
        this.strategies.add(new MapParameterParseStrategy());
        this.strategies.add(new EnumParameterParseStrategy());
    }

    @Override
    public ParameterParserResult parse(Parameter parameter, IRuntimeStack stack) {
        for (IParameterParseStrategy strategy : this.strategies) {
            if (strategy.support(parameter)) {
                 try {
                    return ParameterParserResult.parsed(strategy.parse(parameter, stack));
                 }catch (Exception e){
                     return ParameterParserResult.error(e);
                 }
            }
        }
        return ParameterParserResult.error(new RuntimeException("不支持的参数类型"));
    }

    @Override
    public void registerParameterParseStrategy(IParameterParseStrategy strategy) {
        if (strategy instanceof AbstractParameterParseStrategy absStrategy) {
            absStrategy.setParameterParserManager(this);
        }
        if (this.strategies.contains(strategy)) {
            return;
        }
        this.strategies.add(strategy);
    }
}
