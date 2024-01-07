package cc.catman.coder.workbench.core.value.providers.http;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HttpValueProviderTest {

    @Test
    void run() {
        HttpValueProvider provider=HttpValueProvider.builder()
                .args(
                       HttpValueProviderArgs.builder()
                               .url("https://www.baidu.com")
                               .build()
                ).build();

        Optional<Object> run = provider.run(null);
        System.out.println(run);
    }
}