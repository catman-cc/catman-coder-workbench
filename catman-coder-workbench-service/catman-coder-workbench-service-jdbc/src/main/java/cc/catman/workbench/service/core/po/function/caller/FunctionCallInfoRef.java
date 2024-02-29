package cc.catman.workbench.service.core.po.function.caller;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FunctionCallInfoRef extends CommonRef {
    /**
     * 主键
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 函数ID
     */
    private String functionId;

    /**
     * 函数类型
     */
    private String functionKind;

}
