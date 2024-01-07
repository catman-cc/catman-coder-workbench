package cc.catman.coder.workbench.core.value.providers.samename;

import cc.catman.coder.workbench.core.utils.MapBuilder;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class SameNameValueProviderTest {

    @Test
    void run() {

        ValueProviderContext valueProviderContext= DefaultValueProviderContext.builder()
                .variables(MapBuilder.<String,Object>create().add("name","test")
                        .add("features", Arrays.asList("a","b","c"))
                        .build())
                .build();
        SameNameValueProvider sameNameValueProvider=SameNameValueProvider.builder().name("name").build();
        assert  sameNameValueProvider.run(valueProviderContext).get().equals("test");
    }
}