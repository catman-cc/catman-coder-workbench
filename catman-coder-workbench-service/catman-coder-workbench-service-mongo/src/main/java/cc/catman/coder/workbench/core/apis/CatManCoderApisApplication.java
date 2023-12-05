package cc.catman.coder.workbench.core.apis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.catman.coder.workbench.core.core.common.Group;
import cc.catman.coder.workbench.core.core.common.Scope;
import cc.catman.coder.workbench.core.core.parameter.Parameter;
import cc.catman.coder.workbench.core.core.type.DefaultType;
import cc.catman.coder.workbench.core.core.type.IsType;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.core.type.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import cc.catman.coder.workbench.core.core.job.JobDefinition;

@SpringBootApplication
public class CatManCoderApisApplication {
    @Autowired
    void setMapKeyDotReplacement(MappingMongoConverter mappingMongoConverter) {
        mappingMongoConverter.setMapKeyDotReplacement("_");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CatManCoderApisApplication.class, args);
        MongoOperations bean = run.getBean(MongoOperations.class);

        bean.save(mockJob());
        // DefaultType t = TypeUtils.of(Person.class);
        // bean.save(TypeDefinition.builder().name("ccc").type(t).build());

        // String id = UUID.randomUUID().toString();
        // Parameter parameter = bean.insert(
        // Parameter.builder()
        // .id(id)
        // .group(Group.builder()
        // .id(id)
        // .namespace("cn.jpanda")
        // .name("panda-panda-ooooooooops!")
        // .build())
        // .type(TypeDefinition.builder()
        // .name("info").type(new MapType())
        // .build())
        // .alias(Collections.singletonList("熊猫"))
        // .describe("这里是一个简单的描述信息")
        // .labels(Labels.of(Label.create("cc.catman.coder.apis/example", "true")))
        // .items(
        // Arrays.asList(
        // Parameter.builder()
        // .id(UUID.randomUUID().toString())
        // .scope(Scope.PUBLIC)
        // .items(
        // Arrays.asList(
        // Parameter.builder()
        // .name("ddd")
        // .scope(Scope.PUBLIC)
        // .build()))
        // .build()))
        // .build());
        // System.out.println(parameter);
        // bean.save(parameter);
        // Parameter block = bean.findById("cd554653-e70f-4bb8-8184-3e59bbacd831",
        // Parameter.class);
        // System.out.println(block);
        // Map<Integer, Group> g = new HashMap<>();
        // g.put(123, Group.builder().name("value").namespace("123").build());
        // System.out.println(block);
        ////
        // bean.remove(
        // Query
        // .query(Criteria.byExample(Parameter.builder().id("4378c9a8-3682-4b71-a8aa-76f7aa68cc5d").build())
        // )
        // , Parameter.class
        // );
    }

    public static JobDefinition mockJob() {
        // 构建参数类型
        Group group = Group.builder().namespace("jpanda").name("http-mock").build();
        DefaultType t = TypeUtils.of(Person.class);
        TypeDefinition reqTypeDefinition = TypeDefinition.builder()
                .scope(Scope.PUBLIC)
                .group(group)
                .name("ccc")
                .type(t)
                .build();

        Parameter args = Parameter.builder()
                .group(group)
                .name("body")
                .scope(Scope.PUBLIC)
                .type(reqTypeDefinition)
                .items(Arrays.asList(
                        Parameter.builder()
                                // .id("edcd2d70-c799-4e44-8cc9-61649dbc30eb")
                                .group(group).scope(Scope.PUBLIC)
                                .name("name")
                                .build(),
                        Parameter.builder()
                                .group(group).scope(Scope.PRIVATE)
                                .name("age")
                                .build()))
                .build();
        Parameter res = Parameter.builder()
                .group(group)
                .name("res")
                .scope(Scope.PUBLIC)
                .type(reqTypeDefinition)
                .items(Arrays.asList(
                        Parameter.builder()
                                .group(group).scope(Scope.PUBLIC)
                                .name("res-name")
                                .build(),
                        Parameter.builder()
                                .group(group).scope(Scope.PRIVATE)
                                .name("res-age")
                                .build()))
                .build();
        return JobDefinition.builder()
                .group(group)
                .name("first-http-job")
                .scope(Scope.PUBLIC)
                .requesParameter(args)
                .responseParameter(res)
                .build();
    }

    @IsType
    public static class Person {

        public Person(String name, Integer age, Double score, Boolean graduated, List<Hobby> hobbies,
                HobbyList newHobbies) {
            this.name = name;
            this.age = age;
            this.score = score;
            this.graduated = graduated;
            this.hobbies = hobbies;
            this.newHobbies = newHobbies;
        }

        private String name;
        private Integer age;
        private Double score;

        private Boolean graduated;

        private List<Hobby> hobbies;

        private HobbyList newHobbies;
    }

    @IsType
    public static class Hobby {
        public static Hobby of(String name) {
            return new Hobby(name);
        }

        public Hobby(String name) {
            this.name = name;
        }

        private String name;
    }

    @IsType
    public static class HobbyList extends ArrayList<Hobby> {
        public HobbyList(Hobby... hobbies) {
            super(Arrays.asList(hobbies));
        }
    }
}
