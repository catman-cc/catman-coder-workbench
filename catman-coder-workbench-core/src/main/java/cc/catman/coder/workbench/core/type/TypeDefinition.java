package cc.catman.coder.workbench.core.type;

import java.util.*;
import java.util.stream.Stream;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.common.Mock;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.ComplexType;
import cc.catman.coder.workbench.core.type.complex.ReferType;

import cc.catman.coder.workbench.core.type.raw.RawType;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * 该参数是否必须
     */
    @Builder.Default
    private Boolean required=false;

    /**
     * 该参数的验证规则,如果为空,则不验证
     * 值提供者,必须返回boolean类型
     */
    private IFunctionCallInfo check;

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

    @JsonIgnore
    public ILoopReferenceContext getContext() {
        return Optional.ofNullable(this.getType()).map(DefaultType::getContext).orElse(null);
    }

    @JsonIgnore
    public void setContext(ILoopReferenceContext context) {
       Optional.ofNullable(this.getType()).ifPresent(t -> t.setContext(context));
    }
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
            refers.addAll(ct.getPrivateItems().values().stream().flatMap(i -> {
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
    @JsonIgnore
    public List<TypeDefinition> getAllItems() {
        return this.getType().getAllItems();
    }

    public void addItem(TypeDefinition item) {
        this.type.addItem(item);
    }

    public boolean contains(String id) {
        return this.getType().contains(id);
    }

    public boolean existsInPublic(String id) {
        return this.getType().existsInPublic(id);
    }
    public TypeDefinition getPublic(String id) {
        return Optional.ofNullable(this.getType().getItem(id))
                .filter(item ->item.getScope().isPublic())
                .orElse(null);
    }
}
