package cc.catman.coder.workbench.core.type.complex;

import cc.catman.coder.workbench.core.type.DefaultType;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 复合类型定义,里面会包含一组类型定义信息
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public  class ComplexType extends DefaultType {


}
