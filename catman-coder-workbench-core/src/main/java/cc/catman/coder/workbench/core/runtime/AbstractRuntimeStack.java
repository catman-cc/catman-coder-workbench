package cc.catman.coder.workbench.core.runtime;

import java.util.Map;

public  abstract class AbstractRuntimeStack implements IRuntimeStack{

    @Override
    public IRuntimeStack createChildStack(String prefix, IFunctionCallInfo callInfo, Map<String, Object> presetVariables) {
        IFunctionVariablesTable variablesTable = this.getVariablesTable();
        IFunctionVariablesTable ct = variablesTable.createChildTable(presetVariables);

        // 构建子堆栈数据

        IFunctionVariablesStorage temporaryStorage = variablesTable.getVariablesStorage(EIFunctionVariableScope.TEMPORARY);
        // 创建子变量表,此处主要目的是为了复制临时变量表
        IFunctionVariablesStorage newStorage = temporaryStorage.createChild(presetVariables);


        return null;
    }
}
