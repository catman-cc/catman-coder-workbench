package cc.catman.coder.workbench.core.runtime.stack;

import cc.catman.coder.workbench.core.runtime.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 远程堆栈
 */
public class RemoteRuntimeStack extends AbstractRuntimeStack {
    @Override
    public IRuntimeStack createChildStack(String prefix, IFunctionCallInfo callInfo, Map<String, Object> presetVariables) {
        if (Objects.isNull(presetVariables)){
            presetVariables=new HashMap<>();
        }
        IFunctionVariablesTable variablesTable = this.getVariablesTable();
        IFunctionVariablesTable ct = variablesTable.createChildTable(presetVariables);

        // 构建子堆栈数据

        IFunctionVariablesStorage temporaryStorage = variablesTable.getVariablesStorage(EIFunctionVariableScope.TEMPORARY);
        // 创建子变量表,此处主要目的是为了复制临时变量表
        IFunctionVariablesStorage newStorage = temporaryStorage.createChild(presetVariables);

        return null;
    }

    @Override
    public IRuntimeStack createChildStack(String prefix, IFunctionRuntimeProvider provider, IRuntimeStackDistributor distributor) {

        return null;
    }
}
