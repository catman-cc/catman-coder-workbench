package cc.catman.workbench.service.core.po.function;

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
public class FunctionCallInfoQueueRef extends CommonRef {
    /**
     * 主键
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;

    /**
     * 所属函数调用的id
     */
    private String belongId;

    /**
     * 函数调用类型,是调用队列还是最终执行函数
     */
    private EFunctionCallType type;

    /**
     * 函数返回时,赋值上下文对应的变量名
     */
    private String name;

    /**
     * 对应的函数调用信息id
     */
    private String functionCallInfoId;

    /**
     * 在调用队列中的排序
     */
    private Integer sorting;
}
