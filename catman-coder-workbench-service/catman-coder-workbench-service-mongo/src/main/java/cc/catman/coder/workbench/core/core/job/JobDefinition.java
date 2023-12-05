package cc.catman.coder.workbench.core.core.job;

import java.util.UUID;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import cc.catman.coder.workbench.core.core.Base;
import cc.catman.coder.workbench.core.core.parameter.Parameter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
@Document
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JobDefinition extends Base {

    @MongoId
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * 类型定义名称
     */
    private String name;
    /**
     * 任务请求参数定义
     */
    @Cascade
    private Parameter requesParameter;

    /**
     * 任务响应参数定义
     */
    @Cascade
    private Parameter responseParameter;

}
