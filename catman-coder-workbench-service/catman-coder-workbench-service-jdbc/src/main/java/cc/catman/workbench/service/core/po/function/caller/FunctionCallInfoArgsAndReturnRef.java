package cc.catman.workbench.service.core.po.function.caller;

import cc.catman.workbench.service.core.po.CommonRef;
import cc.catman.workbench.service.core.po.function.EFunctionInfoParamType;
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
public class FunctionCallInfoArgsAndReturnRef extends CommonRef {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private String id;
    /**
     * 所属函数调用者id
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
     * 参数id
     */
    private String parameterId;

    /**
     * 对应的类型定义id
     */
    private String typeDefinitionId;

}
