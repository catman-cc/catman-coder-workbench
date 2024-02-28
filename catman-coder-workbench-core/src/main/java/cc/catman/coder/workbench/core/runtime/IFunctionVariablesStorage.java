package cc.catman.coder.workbench.core.runtime;

import java.util.*;

/**
 * 函数变量
 * 用于存储函数的参数
 */
public interface IFunctionVariablesStorage {

    /**
     * 获取父级参数存储器
     *
     * @return 父级参数
     */
    Optional<IFunctionVariablesStorage> getParent();

    IFunctionVariablesStorage createChild(Map<String,Object> variables);

    /**
     * 获取参数映射表
     *
     * @return 参数映射表
     */
    Map<String, String> getAlias();


    Map<String, Object> getOriginalVariables();

    /**
     * 设置参数,如果参数已经存在,则替换参数
     * @param name 参数名称
     * @param value 参数值
     * @return 如果是全新变量, 则返回true, 否则返回false(替换了变量)
     */
    boolean setVariable(String name, Object value);

    void add(Map<String,Object> variables);

    /**
     * 获取所有参数定义,包括父级参数,并且将参数名称做别名转换操作
     * 需要注意的是,此处的参数表是一个只读的参数表,如果需要修改参数,请使用{@link #getOriginalVariables()}
     * @return 参数定义
     */
    default Map<String, Object> getVariables() {
        final Map<String, Object> variables = new HashMap<>();
        getParent().ifPresent(parent -> {
            Map<String, Object> pvs = parent.getVariables();
            variables.putAll(pvs);
            pvs.forEach((k, v) -> variables.put(doAlias(k), v));
        });
        getOriginalVariables().forEach((k, v) -> variables.put(doAlias(k), v));
        // 返回一个只读的参数表
        return Collections.unmodifiableMap(variables);
    }

    default Object getOriginalVariable(String name){
        Map<String, Object> originalVariables = getOriginalVariables();
        if (originalVariables.containsKey(name)){
            return originalVariables.get(name);
        }
        // 如果没有找到,有可能是别名,尝试查找别名,注意此处传来的name可能是别名,但是集合内的key是原始名称,所以需要做一次转换
        Map<String, String> alias = getAlias();
        if(alias != null){
            String originalName = alias.get(name);
            if (originalName != null){
                return originalVariables.get(originalName);
            }
        }
        return null;
    }
    default Object getVariable(String name){
        return getVariables().get(name);
    }

    /**
     * 将参数名称做别名转换操作
     *
     * @param name 参数名称
     * @return 转换后的参数名称
     */
    default String doAlias(String name) {
        Map<String, String> alias = getAlias();
        if (alias == null) {
            return name;
        }
        String aliasName = alias.get(name);
        if (aliasName == null) {
            return name;
        }
        return aliasName;
    }
}
