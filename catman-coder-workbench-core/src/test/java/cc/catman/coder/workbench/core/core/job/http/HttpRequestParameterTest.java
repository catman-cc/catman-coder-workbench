package cc.catman.coder.workbench.core.core.job.http;

import cc.catman.coder.workbench.core.job.http.HttpRequestParameter;
import org.junit.Test;

import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeUtils;

public class HttpRequestParameterTest {

    @Test
    public void t() {
        DefaultType dType = TypeUtils.of(HttpRequestParameter.class);
        System.out.println(dType);
    }
}
