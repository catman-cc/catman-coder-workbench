package cc.catman.workbench.service.core.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import cc.catman.plugin.core.label.Labels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * 基础实体类,包含了最基础的数据定义
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Base {
    private String id;
    /**
     * 参数所属组,进行数据隔离
     */
    protected Group group;

    /**
     * 是否公开该类型定义,当该类型定义被公开时,外部可以对该类型进行引用
     */
    @Builder.Default
    protected Scope scope = Scope.PRIVATE;

    /**
     * 类型定义的标签,通过标签可以将类型定义分组
     */
    @Builder.Default
    protected List<Tag> tags=new ArrayList<>();

    /**
     * 别名
     */
    @Builder.Default
    protected List<String> alias=new ArrayList<>();

    /**
     * 参数的插件化数据
     */
    @Builder.Default
    protected Labels labels=Labels.empty();

    /**
     * 参数对应的wiki内容,这个可能需要修改,还没想好
     */
    protected String wiki;

    public  <T extends cc.catman.coder.workbench.core.Base> T mergeInto(T other){
        Optional.ofNullable(this.getGroup()).ifPresent(g->{
            other.setGroup(g.convert());
        });
        Optional.ofNullable(this.getTags()).ifPresent(tags->{
            other.setTags(tags.stream().map(Tag::convert).toList());
        });
        other.setScope(this.getScope().convert());
        other.setAlias(this.getAlias());
        other.setLabels(this.getLabels());
        other.setWiki(this.getWiki());
        return other;
    }

    public  <T extends Base> T mergeInto(T other){
        Optional.ofNullable(this.getGroup()).ifPresent(other::setGroup);
        Optional.ofNullable(this.getTags()).ifPresent(other::setTags);
        other.setScope(this.getScope());
        other.setAlias(this.getAlias());
        other.setLabels(this.getLabels());
        other.setWiki(this.getWiki());
        return other;
    }
}
