package cc.catman.coder.workbench.core.core.entity;

/**
 * 实体定义
 */
public class EntityDefinition {
    private String name;
    private Entity<?> entity;

    public EntityDefinition(String name, Entity<?> entity) {
        this.name = name;
        this.entity = entity;
    }
}
