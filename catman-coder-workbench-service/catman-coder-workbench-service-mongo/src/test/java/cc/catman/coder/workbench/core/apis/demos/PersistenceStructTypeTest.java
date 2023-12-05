package cc.catman.coder.workbench.core.apis.demos;

import cc.catman.coder.workbench.core.core.entity.Entity;
import cc.catman.coder.workbench.core.core.common.Group;
import cc.catman.coder.workbench.core.core.type.Type;
import cc.catman.coder.workbench.core.core.type.TypeUtils;
import cc.catman.plugin.core.label.Label;
import cc.catman.plugin.core.label.Labels;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Collections;
import java.util.UUID;


//@SpringBootTest
public class PersistenceStructTypeTest {
//    @Autowired
    private MongoOperations mongoOperations;

    final private ObjectMapper mapper=new JsonMapper().enable(SerializationFeature.INDENT_OUTPUT);
    @Test
    public void save(){
        String id = UUID.randomUUID().toString();
        Parameter parameter =
                Parameter.builder()
                        .id(id)
                        .group(Group.builder()
                                .id(id)
                                .namespace("cn.jpanda")
                                .name("panda-panda-ooooooooops!")
                                .build())
                        .className("jpanda")
                        .alias(Collections.singletonList("熊猫"))
                        .describe("这里是一个简单的描述信息")
                        .labels(Labels.of(Label.create("cc.catman.coder.apis/example", "true")))
//                        .fields(Arrays.asList(
//                                Parameter.builder()
//                                        .id(UUID.randomUUID().toString())
//                                        .name("field1")
//                                        .build(),
//                                Parameter.builder()
//                                        .id(UUID.randomUUID().toString())
//                                        .name("field2")
//                                        .build(),
//                                Parameter.builder()
//                                        .id(UUID.randomUUID().toString())
//                                        .name("field3")
//                                        .build(),
//                                Parameter.builder()
//                                        .id(UUID.randomUUID().toString())
//                                        .name("field4")
//                                        .build()
//                        ))
                        .build();

        Type of = TypeUtils.of(parameter);
        Entity entity = of.toEntity(parameter);

        System.out.println(of);
    }
}