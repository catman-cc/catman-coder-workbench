package cc.catman.coder.workbench.core.core.job.http;

import org.junit.Test;

import cc.catman.coder.workbench.core.core.type.DefaultType;
import cc.catman.coder.workbench.core.core.type.TypeUtils;

public class HttpRequestParameterTest {

    @Test
    public void t() {
        DefaultType dType = TypeUtils.of(HttpRequestParameter.class);
        System.out.println(dType);
    }
}
