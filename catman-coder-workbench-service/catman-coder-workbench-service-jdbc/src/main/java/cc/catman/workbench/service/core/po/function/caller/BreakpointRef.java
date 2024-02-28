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
public class BreakpointRef extends CommonRef {

    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;
    /**
     * 断点对应的函数id
     */
    private String functionId;

    /**
     * 断点对应的函数调用者id,其实就是belongId
     */
    private String functionCallerId;

    /**
     * 断点对应的行号,此处的行号和代码行号不一样,这里的行号其实是Function提供的断点index
     */
    private int lineNumber;

    /**
     * 断点是否启用
     */
    private  boolean isEnable;

    /**
     * 断点对应的变量参数id
     */
    private  String variablesParameterId;

    private Integer sorting;

}
