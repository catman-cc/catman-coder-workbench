package cc.catman.coder.workbench.core.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultFunctionVariablesTable implements IFunctionVariablesTable{
    private Map<EIFunctionVariableScope, IFunctionVariablesStorage> variablesStorages;

    public static DefaultFunctionVariablesTable of(Map<String,Object> presetVariables){
        DefaultFunctionVariablesTable table = new DefaultFunctionVariablesTable();
        table.getVariablesStorage(EIFunctionVariableScope.LOCAL).add(presetVariables);
        return table;
    }

    public static DefaultFunctionVariablesTable of(EIFunctionVariableScope scope,Map<String,Object> presetVariables){
        DefaultFunctionVariablesTable table = new DefaultFunctionVariablesTable();
        table.getVariablesStorage(scope).add(presetVariables);
        return table;
    }

    public DefaultFunctionVariablesTable() {
        this.variablesStorages = new HashMap<>();
        this.variablesStorages.put(EIFunctionVariableScope.GLOBAL,new DefaultFunctionVariablesStorage());
        this.variablesStorages.put(EIFunctionVariableScope.LOCAL,new DefaultFunctionVariablesStorage());
        this.variablesStorages.put(EIFunctionVariableScope.TEMPORARY,new DefaultFunctionVariablesStorage());
    }

    public DefaultFunctionVariablesTable(Map<EIFunctionVariableScope, IFunctionVariablesStorage> variablesStorages) {
        this.variablesStorages = variablesStorages;
    }

    @Override
    public Map<EIFunctionVariableScope, IFunctionVariablesStorage> getVariablesStorages() {
        return this.variablesStorages;
    }

    @Override
    public IFunctionVariablesStorage getVariablesStorage(EIFunctionVariableScope scope) {
        return this.variablesStorages.get(scope);
    }

    @Override
    public Object getVariable(String name) {
        if (this.existVariable(name,EIFunctionVariableScope.TEMPORARY)){
            return this.getVariable(name,EIFunctionVariableScope.TEMPORARY);
        }
        if (this.existVariable(name,EIFunctionVariableScope.LOCAL)){
            return this.getVariable(name,EIFunctionVariableScope.LOCAL);
        }
        if (this.existVariable(name,EIFunctionVariableScope.GLOBAL)){
            return this.getVariable(name,EIFunctionVariableScope.GLOBAL);
        }
        return null;
    }

    @Override
    public Object getVariable(String name, EIFunctionVariableScope scope) {
        return this.getVariablesStorage(scope).getVariables().get(name);
    }

    @Override
    public boolean existVariable(String name) {
        return this.existVariable(name,EIFunctionVariableScope.TEMPORARY) ||
                this.existVariable(name,EIFunctionVariableScope.LOCAL) ||
                this.existVariable(name,EIFunctionVariableScope.GLOBAL);
    }

    @Override
    public boolean existVariable(String name, EIFunctionVariableScope scope) {
        return this.getVariablesStorage(scope).getVariables().containsKey(name);
    }

    @Override
    public boolean setVariable(String name, Object value) {
       return this.getVariablesStorage(EIFunctionVariableScope.TEMPORARY).setVariable(name,value);
    }

    @Override
    public boolean setVariable(String name, Object value, EIFunctionVariableScope scope) {
       return this.getVariablesStorage(scope).setVariable(name,value);
    }

    @Override
    public <T> T getVariable(String name, Class<T> clazz) {
        Object variable = this.getVariable(name);
        if (variable != null){
            // 此处有可能抛出类型转换异常
            return clazz.cast(variable);
        }
        return null;
    }

    @Override
    public Map<String, Object> getVariables() {
        Map<String,Object> variables=new HashMap<>();
        // 依次填充全局变量,局部变量,临时变量
        variables.putAll(this.getVariablesStorage(EIFunctionVariableScope.GLOBAL).getVariables());
        variables.putAll(this.getVariablesStorage(EIFunctionVariableScope.LOCAL).getVariables());
        variables.putAll(this.getVariablesStorage(EIFunctionVariableScope.TEMPORARY).getVariables());
        return variables;
    }

    @Override
    public IFunctionVariablesTable createChildTable(Map<String, Object> variables) {
        Map<EIFunctionVariableScope,IFunctionVariablesStorage> storages=new HashMap<>();
        storages.put(EIFunctionVariableScope.GLOBAL,this.getVariablesStorage(EIFunctionVariableScope.GLOBAL).createChild(variables));
        storages.put(EIFunctionVariableScope.LOCAL,this.getVariablesStorage(EIFunctionVariableScope.LOCAL));

        IFunctionVariablesStorage temporary = this.getVariablesStorage(EIFunctionVariableScope.TEMPORARY);
        IFunctionVariablesStorage childTemporary = temporary.createChild(variables);
        storages.put(EIFunctionVariableScope.TEMPORARY,childTemporary);

        return new DefaultFunctionVariablesTable(storages);
    }

    @Override
    public void readAll(IFunctionVariablesTable table){
        for (EIFunctionVariableScope scope : EIFunctionVariableScope.values()) {
           Optional.ofNullable(table.getVariablesStorage(scope)).ifPresent(storage -> {
               this.getVariablesStorage(scope).add(storage.getOriginalVariables());
           });
        }
    }
}
