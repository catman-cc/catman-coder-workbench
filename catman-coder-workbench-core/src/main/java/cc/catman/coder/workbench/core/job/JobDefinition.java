package cc.catman.coder.workbench.core.job;

import java.util.UUID;

import cc.catman.coder.workbench.core.Base;

import cc.catman.coder.workbench.core.parameter.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 任务接口定义
 *
 * 一个任务至少要包含入参和出参
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JobDefinition extends Base {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * 类型定义名称
     */
    private String name;
    /**
     * 任务请求参数定义
     */
    private Parameter requesParameter;

    /**
     * 任务响应参数定义
     */
    private Parameter responseParameter;

}
