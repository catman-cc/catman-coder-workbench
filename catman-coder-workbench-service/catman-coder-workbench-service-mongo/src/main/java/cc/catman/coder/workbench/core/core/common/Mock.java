package cc.catman.coder.workbench.core.core.common;

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
