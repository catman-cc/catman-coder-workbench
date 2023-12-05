package cc.catman.coder.workbench.core.apis.demos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cc.catman.coder.workbench.core.core.common.Group;
import cc.catman.coder.workbench.core.core.common.Mock;
import cc.catman.coder.workbench.core.core.common.Scope;
import cc.catman.coder.workbench.core.core.type.IsType;
import cc.catman.coder.workbench.core.core.type.Type;
import cc.catman.coder.workbench.core.core.type.complex.StructType;
import cc.catman.plugin.core.label.Labels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@IsType
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("parameter")
@EqualsAndHashCode(callSuper = true)
public class Parameter extends StructType {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * 参数的可用范围
     */
    @Builder.Default
    protected Scope scope = Scope.PRIVATE;

    /**
     * 参数的默认值
     */
    protected String value;

    /**
     * 参数的别名
     */
    protected List<String> alias;

    /**
     * 参数的简短描述
     */
    protected String describe;

    /**
     * 参数的插件化数据
     */
    protected Labels labels;

    /**
     * 参数所属组
     */
    @Cascade
    protected Group group;
    // /**
    // * 构成参数的字段,我在想要不要将一部分字段定义作为内嵌数据存储.那些不需要被外部引用的参数定义就直接内嵌存储.
    // * 但是内嵌存储又涉及到字段排序的问题,考虑加一个before或者next做一个单链表的排序?
    // */
    // @Cascade(
    // when = "#each.scope==T(cc.catman.coder.core.common.Scope).PUBLIC"
    // )
    // protected List<Parameter> fields;

    /**
     * mock数据
     */
    protected Mock mock;

    /**
     * 以递归的方式获取所有被包含的公共类型的参数定义,需要注意的是,当匹配到子定义是公开的时,会直接返回该自定义,不会继续深入
     */
    public List<Parameter> recursionListPublic() {
        return new ArrayList<>(this.items.stream()
                .flatMap(i -> {
                    Type type = i.getType();
                    if (type instanceof Parameter p) {
                        if (Scope.PUBLIC.equals(p.scope)) {
                            return Stream.of(p);
                        }
                        return p.recursionListPublic().stream();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList());
    }
}
