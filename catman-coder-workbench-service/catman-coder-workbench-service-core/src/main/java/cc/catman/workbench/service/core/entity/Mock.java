package cc.catman.workbench.service.core.entity;

import lombok.Data;

@Data
public class Mock {

    /**
     * mock的类型
     */
    private String kind;
    /**
     * mock的内容
     */
    private String value;
}
