package cc.catman.coder.workbench.core.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultFunctionVariablesStorage implements IFunctionVariablesStorage{
    private Map<String, Object> variables;
    private Map<String,String> alias;

    private IFunctionVariablesStorage parent;

    public DefaultFunctionVariablesStorage() {
        this(null);
    }

    public DefaultFunctionVariablesStorage(IFunctionVariablesStorage parent) {
        this(new HashMap<>(),new HashMap<>(),parent);

    }

    public DefaultFunctionVariablesStorage(Map<String, Object> variables, Map<String, String> alias, IFunctionVariablesStorage parent) {
        this.variables = variables;
        this.alias = alias;
        this.parent = parent;
    }

    @Override
    public Optional<IFunctionVariablesStorage> getParent() {
        return Optional.of(parent);
    }

    @Override
    public IFunctionVariablesStorage createChild(Map<String, Object> variables) {
        DefaultFunctionVariablesStorage storage = new DefaultFunctionVariablesStorage(this);
        storage.add(variables);
        return storage;
    }

    @Override
    public Map<String, String> getAlias() {
        return this.alias;
    }

    @Override
    public Map<String, Object> getOriginalVariables() {
        return this.variables;
    }

    @Override
    public boolean setVariable(String name, Object value) {
        boolean exist=this.variables.containsKey(name);
        this.variables.put(name, value);
        return exist;
    }

    @Override
    public void add(Map<String, Object> variables) {
        this.variables.putAll(variables);
    }
}
