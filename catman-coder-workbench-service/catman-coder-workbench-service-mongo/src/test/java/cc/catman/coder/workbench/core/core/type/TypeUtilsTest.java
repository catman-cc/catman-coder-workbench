package cc.catman.coder.workbench.core.core.type;

import cc.catman.coder.workbench.core.core.entity.Entity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TypeUtilsTest {
    private ObjectMapper objectMapper=new JsonMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    @Test
    void of() throws JsonProcessingException {
        Type t = TypeUtils.of(Person.class);
        System.out.println(objectMapper.writeValueAsString(t));
        Entity<?> entity = t.toEntity(new Person("panda", 16, 100.0, true, Arrays.asList(Hobby.of("study")),new HobbyList(new Hobby("work"))));
        Object object = entity.toObject();

        System.out.println(objectMapper.writeValueAsString(object));
    }
    @IsType
    public static class Person{

        public Person(String name, Integer age, Double score, Boolean graduated, List<Hobby> hobbies,HobbyList  newHobbies) {
            this.name = name;
            this.age = age;
            this.score = score;
            this.graduated = graduated;
            this.hobbies = hobbies;
            this.newHobbies=newHobbies;
        }

        private String name;
        private Integer age;
        private Double  score;

        private Boolean graduated;

        private List<Hobby> hobbies;

        private HobbyList newHobbies;
    }

    @IsType
    public static class Hobby{
        public static Hobby of(String name){
            return new Hobby(name);
        }

        public Hobby(String name) {
            this.name = name;
        }

        private String name;
    }

    @IsType
    public static class HobbyList extends ArrayList<Hobby>{
        public HobbyList(Hobby...hobbies) {
            super(Arrays.asList(hobbies));
        }
    }
}