package cc.catman.coder.workbench.core.runtime;

import java.util.Map;

/**
 * 函数变量表
 * 用于存储函数的变量信息
 */
public interface IFunctionVariablesTable {

    /**
     * 获取变量存储器
     *
     * @return 变量存储器
     */
    Map<EIFunctionVariableScope, IFunctionVariablesStorage> getVariablesStorages();

    /**
     * 获取变量存储器
     *
     * @param scope 变量作用域
     * @return 变量存储器
     */
    IFunctionVariablesStorage getVariablesStorage(EIFunctionVariableScope scope);

    /**
     * 获取变量,此操作将会从所有的变量存储器中获取变量
     * @param name 变量名
     * @return 变量
     */
    Object getVariable(String name);

    /**
     * 获取变量,此操作将会从指定的变量存储器中获取变量
     * @param name 变量名
     * @param scope 变量作用域
     * @return 变量
     */
    Object getVariable(String name, EIFunctionVariableScope scope);

    boolean existVariable(String name);

    boolean existVariable(String name, EIFunctionVariableScope scope);

    boolean setVariable(String name, Object value);

    boolean setVariable(String name, Object value, EIFunctionVariableScope scope);

    <T> T getVariable(String name, Class<T> clazz);

    Map<String,Object> getVariables();

    IFunctionVariablesTable createChildTable(Map<String,Object> variables);

    void readAll(IFunctionVariablesTable table);
}
