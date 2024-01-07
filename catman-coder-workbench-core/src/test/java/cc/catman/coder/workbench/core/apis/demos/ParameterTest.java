//package cc.catman.coder.workbench.core.apis.demos;
//
//import cc.catman.coder.workbench.core.common.Group;
//import cc.catman.coder.workbench.core.common.Scope;
//import cc.catman.plugin.core.label.Label;
//import cc.catman.plugin.core.label.Labels;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//
//@SpringBootTest
//public class ParameterTest {
//
//    @Autowired
//    private MongoOperations mongoOperations;
//
//
//
//    final private ObjectMapper mapper=new JsonMapper().enable(SerializationFeature.INDENT_OUTPUT);
//    @BeforeEach
//    public void setUp() {
//        String id = UUID.randomUUID().toString();
//        Parameter parameter = mongoOperations.insert(
//                Parameter.builder()
//                        .id(id)
//                        .group(Group.builder()
//                                .id(id)
//                                .namespace("cn.jpanda")
//                                .name("panda-panda-ooooooooops!")
//                                .build())
//                        .className("jpanda")
//                        .alias(Collections.singletonList("熊猫"))
//                        .describe("这里是一个简单的描述信息")
//                        .labels(Labels.of(Label.create("cc.catman.coder.apis/example", "true")))
////                        .fields(Arrays.asList(
////                                Parameter.builder()
////                                        .id(UUID.randomUUID().toString())
////                                        .name("field1")
////                                        .build(),
////                                Parameter.builder()
////                                        .id(UUID.randomUUID().toString())
////                                        .name("field2")
////                                        .build(),
////                                Parameter.builder()
////                                        .id(UUID.randomUUID().toString())
////                                        .name("field3")
////                                        .build(),
////                                Parameter.builder()
////                                        .id(UUID.randomUUID().toString())
////                                        .name("field4")
////                                        .build()
////                        ))
//                        .build()
//        );
//    }
//
//    @Test
//    public void findByScope() throws JsonProcessingException {
//        List<Parameter> parameters = mongoOperations.find(Query.query(Criteria.byExample(Parameter.builder().scope(Scope.PRIVATE).build())), Parameter.class);
//
//        System.out.println(new String(mapper.writeValueAsBytes(parameters)));
//    }
//    @Test
//    public void findByScopePublic(){
//        List<Parameter> parameters = mongoOperations.find(Query.query(Criteria.byExample(Parameter.builder().scope(Scope.PUBLIC).build())), Parameter.class);
//        System.out.println(parameters);
//    }
//}