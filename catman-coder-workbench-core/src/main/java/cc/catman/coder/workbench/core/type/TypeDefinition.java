package cc.catman.coder.workbench.core.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.common.Mock;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.ComplexType;
import cc.catman.coder.workbench.core.type.complex.ReferType;

import cc.catman.coder.workbench.core.type.raw.RawType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TypeDefinition extends Base {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * 类型定义名称
     */
    private String name;

    /**
     * 类型定义
     */
    private DefaultType type;

    /**
     * 参数的默认值
     */
    protected String defaultValue;

    /**
     * 参数的简短描述
     */
    protected String describe;

    /**
     * mock数据
     */
    protected Mock mock;

    /**
     * 该方法的目的是查找出,当前类型定义引用了哪些公开类型定义
     */
    public List<TypeDefinition> recursionListPublic() {
        return recursionListPublic(this.getType());
    }

    public List<TypeDefinition> recursionListPublic(Type type) {
        List<TypeDefinition> list = new ArrayList<>();
        if (type instanceof RawType<?>) {
            return list;
        }
        Set<TypeDefinition> refers = new HashSet<>();

        // 复合类型,map,struct,refer
        if (type instanceof ComplexType ct) {
            refers.addAll(ct.getItems().stream().flatMap(i -> {
                if (Scope.PUBLIC.equals(i.scope)) {
                    return Stream.of(i);
                }
                return i.recursionListPublic().stream();
            }).toList());
        }
        // refer一定是引用类型
        if (type instanceof ReferType rt) {
            refers.add(rt.getTypeDefinition());
        }

        // 数组类型
        if (type instanceof ArrayType at) {
            refers.addAll(recursionListPublic(at.getElement()));
        }

        list.addAll(refers);
        return list;
    }
}
