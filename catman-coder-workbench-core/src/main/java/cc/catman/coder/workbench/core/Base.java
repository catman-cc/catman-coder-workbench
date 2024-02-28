package cc.catman.coder.workbench.core;

import java.util.List;

import cc.catman.coder.workbench.core.common.Group;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.common.Tag;
import cc.catman.plugin.core.label.Labels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Base {
    /**
     * 参数所属组,进行数据隔离
     */
    protected Group group;

    /**
     * 被标注资源的版本信息,根据资源的版本信息,访问者可以考虑如何处理资源的版本信息
     */
    protected String version;

    /**
     * 是否公开该类型定义,当该类型定义被公开时,外部可以对该类型进行引用
     */
    @Builder.Default
    protected Scope scope = Scope.PRIVATE;

    /**
     * 类型定义的标签,通过标签可以将类型定义分组
     */
//    @Cascade
    protected List<Tag> tags;

    /**
     * 参数的别名
     */
    protected List<String> alias;

    /**
     * 参数的插件化数据
     */
    protected Labels labels;

    /**
     * 参数对应的wiki内容,这个可能需要修改,还没想好
     */
    protected String wiki;

    /**
     * 对应的资源只允许进行有限的修改
     */
    protected boolean limitedChanges;

}
