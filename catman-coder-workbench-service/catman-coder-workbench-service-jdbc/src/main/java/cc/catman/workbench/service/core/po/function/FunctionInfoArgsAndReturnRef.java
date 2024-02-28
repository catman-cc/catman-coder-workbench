package cc.catman.workbench.service.core.po.function;

import cc.catman.workbench.service.core.po.CommonRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * 函数信息的参数和返回值定义
 */
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfoArgsAndReturnRef extends CommonRef {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    /**
     * 所属函数id
     */
    private String belongId;
    /**
     *  参数或返回值的类型
     */
    private EFunctionInfoParamType type;

    /**
     * 参数或返回值的名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sorting;

    /**
     * 对应的类型定义id
     */
    private String typeDefinitionId;

}
