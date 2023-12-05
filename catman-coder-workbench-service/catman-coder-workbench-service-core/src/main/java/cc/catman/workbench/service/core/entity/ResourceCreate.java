package cc.catman.workbench.service.core.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源创建配置
 * 可以在创建资源时指定资源的详细信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceCreate extends Resource{
    private String type;
    private Map<String,Object> config=new HashMap<>();
}
