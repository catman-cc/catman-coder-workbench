package cc.catman.coder.workbench.core.label;


import cc.catman.coder.workbench.core.label.equal.*;
import cc.catman.coder.workbench.core.utils.MapBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class AbstractLabelSelectorTest {

    @Test
    void between(){
        ILabelSelector<Object> between = AllLabelSelector.builder()
                .kind("Any")
                .match("for-any")
                .build()
                .addRule(GteLabelSelector.builder()
                        .kind("Gte")
                        .match("for-any")
                        .value(100)
                        .build())
                .addRule(LteLabelSelector.builder()
                        .kind("Lte")
                        .match("for-any")
                        .value(200)
                        .build());
        betweenCheck(between);
    }

    @Test
    @SneakyThrows
    void betweenJson(){
        String json= """
                {
                    "match": "for-any",
                    "kind": "All",
                    "rules": [
                        {
                            "kind": "Gte",
                            "value": 100
                        },
                        {
                            "kind": "Lte",
                            "value": 200
                        }
                    ]
                }
                """;
        ILabelSelector<?> selector = DefaultLabelSelectorFactory.createDefault().create(new ObjectMapper().readTree(json));
        betweenCheck(selector);

        assert  DefaultLabelSelectorContext.createDefault().valid(json, MapBuilder.create().put("for-any", 150).build());
    }
    private static void betweenCheck(ILabelSelector<?> between) {
        assert   DefaultLabelSelectorContext.createDefault().valid(between, MapBuilder.create().put("for-any", 150).build());
        assert   DefaultLabelSelectorContext.createDefault().valid(between, MapBuilder.create().put("for-any", 100).build());
        assert   DefaultLabelSelectorContext.createDefault().valid(between, MapBuilder.create().put("for-any", 200).build());

        assert   DefaultLabelSelectorContext.createDefault().valid(between, MapBuilder.create().put("for-any", "101").build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(between, MapBuilder.create().put("for-any", 99).build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(between, MapBuilder.create().put("for-any", null).build());
    }

    @Test
    void in(){
        InLabelSelector build = InLabelSelector.builder()
                .kind("In")
                .match("for-any")
                .value(Arrays.asList("name", "age"))
                .build();
        assert   DefaultLabelSelectorContext.createDefault().valid(build, MapBuilder.create().put("for-any", "name").build());
        assert   DefaultLabelSelectorContext.createDefault().valid(build, MapBuilder.create().put("for-any", "age").build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(build, MapBuilder.create().put("for-any", "age1").build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(build, MapBuilder.create().put("for-any2", "age").build());
    }

    @Test
    void contain(){
        ContainLabelSelector containLabelSelector = ContainLabelSelector.builder()
                .kind("Contains")
                .match("for-any")
                .value("name")
                .build();
        // test null
        assert   !DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", null).build());
        // test string
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", "name").build());
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", "name1").build());
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", "1name").build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", "1nam1").build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any2", "name").build());

        // test list
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", Arrays.asList("name","age")).build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", Arrays.asList("name1","age")).build());
    }

    @Test
    void containNumber(){
        ContainLabelSelector containLabelSelector = ContainLabelSelector.builder()
                .kind("Contains")
                .match("for-any")
                .value("123")
                .build();
        // test number
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", 123).build());
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", 1234).build());
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", 12345).build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", 23145).build());

        // test list number
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", Arrays.asList(123,1234)).build());
        assert   !DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", Arrays.asList(1234,12345)).build());
        // test mix
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", Arrays.asList(123,"1223")).build());
        assert   DefaultLabelSelectorContext.createDefault().valid(containLabelSelector, MapBuilder.create().put("for-any", Arrays.asList("123",231)).build());
    }


}