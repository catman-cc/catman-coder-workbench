package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个最简单的类型分析器,可以通过一个简单的描述来创建一个类型定义
 *  例如:
 *      {
 *          "name":"user",
 *          "type":"object",
 *          "items":[
 *          {
 *          "name":"name",
 *          "type":"string"
 *          },
 *      }
 * 内部嵌入了一个简单的json解析器,可以通过json来描述一个类型
 */
public class SimpleTypeAnalyzer {

    TypeDesc typeDesc;

    private final static ObjectMapper objectMapper=new JsonMapper();

    private ILoopReferenceContext context=ILoopReferenceContext.create();

    public static SimpleTypeAnalyzer of(TypeDesc typeDesc){
        return new SimpleTypeAnalyzer(typeDesc);
    }

    public static SimpleTypeAnalyzer of(TypeDesc typeDesc,ILoopReferenceContext context){
        return new SimpleTypeAnalyzer(typeDesc,context);
    }

    @SneakyThrows
    public static SimpleTypeAnalyzer of(String json){
        return new SimpleTypeAnalyzer(objectMapper.readValue(json,TypeDesc.class));
    }

    public SimpleTypeAnalyzer(TypeDesc typeDesc){
        this.typeDesc = typeDesc;
    }

    public SimpleTypeAnalyzer(TypeDesc typeDesc, ILoopReferenceContext context) {
        this.typeDesc = typeDesc;
        this.context = context;
    }

    public TypeDefinition analyzer(){
        DefaultType type = DefaultType.builder()
                .typeName(typeDesc.type)
                .context(context)
                .build();
        TypeDefinition typeDefinition = TypeDefinition.builder()
                .type(type)
                .name(typeDesc.name)
                .describe(typeDesc.desc)
                .build();
        context.add(typeDefinition);
        if(this.typeDesc.items != null){
            this.typeDesc.items.forEach(item -> {
                TypeDefinition itemTypeDefinition = SimpleTypeAnalyzer.of(item,context).analyzer();
                type.addItem(itemTypeDefinition);
            });
        }
        return typeDefinition;
    }

    @Setter
    @Getter
    public static class TypeDesc{
        String name;
        String type;
        String desc;
        boolean required;
        List<TypeDesc> items=new ArrayList<>();

        public static TypeDesc create(){
            return new TypeDesc();
        }

        public TypeDesc name(String name){
            this.name = name;
            return this;
        }

        public TypeDesc type(String type){
            this.type = type;
            return this;
        }

        public TypeDesc desc(String desc){
            this.desc = desc;
            return this;
        }
        public TypeDesc required(boolean required){
            this.required = required;
            return this;
        }

        public TypeDesc items(List<TypeDesc> items){
            this.items = items;
            return this;
        }

        public TypeDesc add(TypeDesc item){
            this.items.add(item);
            return this;
        }

        public TypeDesc add(String name, String type){
            this.items.add(TypeDesc.create().name(name).type(type));
            return this;
        }

        public TypeDesc add(String name, String type, String desc){
            this.items.add(TypeDesc.create().name(name).type(type).desc(desc));
            return this;
        }

        public TypeDesc add(String name, String type, String desc, boolean required){
            this.items.add(TypeDesc.create().name(name).type(type).desc(desc).required(required));
            return this;
        }

        public TypeDesc add(String name, Class<?> type){
            // 偷个懒
            TypeDefinition analyze = TypeDefinitionAnalyzer.builder()
                    .object(type)
                    .name(name)
                    .build()
                    .analyze();
            return add(name,analyze.getType().getTypeName());
        }
    }
}
