package cc.catman.workbench.service.core.po.function;

import cc.catman.coder.workbench.core.runtime.debug.BreakpointInformation;
import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import java.util.List;

/**
 * 函数定义
 */
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfoRef extends CommonRef {
    /**
     * 唯一标志
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String  id;

    /**
     * 函数名称
     */
    private String name;

    /**
     * 函数类型
     */
    private String kind;

    /**
     * 函数的简短描述
     */
    @Column(name = "`describe`")
    private String describe;

    public List<BreakpointInformation> getBreakpointInformation() {
        return null;
    }
}
