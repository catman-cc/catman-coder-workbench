package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Supplier;

/**
 * 参数定义
 * 一个参数定义的结构取决于他所依赖的类型定义
 */
@Slf4j
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Parameter extends Base {
    @Getter
    @Setter
    @Builder.Default
    private String id = Constants.TEMP_ID_SUFFIX + UUID.randomUUID();

    /**
     * 参数名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 参数的简短描述
     */
    @Getter
    @Setter
    protected String describe;


    /**
     * 参数的类型定义
     */
    private TypeDefinition type;

    /**
     * 参数的类型定义标志
     */
    private String typeId;

    /**
     * 参数的值
     */
    @Getter
    @Setter
    private ValueProviderDefinition value;

    /**
     * 参数的默认值
     */
    @Getter
    @Setter
    private ValueProviderDefinition defaultValue;

    /**
     *  只有触发器为空,或者触发器返回true时,才会解析当前参数
     *  用于解决递归引用的问题,举例说明,假设有一个参数A,他的值提取器是一个对象,该对象的属性是一个数组,数组的元素是A本身
     *  这样的话,在解析A的时候,会进入死循环,因此需要一个触发器来判断是否需要解析当前参数?? 这种方式不行,因为是分布式的系统,这种值会导致递归
     *
     *  理论上就是A-B-A引用,但是两个A其实不是一个对象,因此不会出现死循环,但是在执行时,如何判断第二个A要不要初始化???
     *  这就用到了触发器,比如,要求B必须初始化了,或者B的某个属性必须有值了,才会初始化A,否则就不会初始化A
     */
    @Getter
    @Setter
    private ValueProviderDefinition trigger;


    /**
     * 参数是否为必填项,如果当前参数是必填项,在解析时,会进行验证
     */
    @Getter
    @Setter
    private boolean required;

    /**
     *  是否跳过子节点的解析,该参数可以在前端进行设置
     *  如果当前参数是一个对象,并且该属性为true,则不会解析该对象的子节点
     *  同时,如果一个参数的所有子节点都是用了父节点取值器,那么该属性也会被设置为true
     */
    @Getter
    @Setter
    private boolean skipChildFlag;

    /**
     * 当该参数无法获取值时,跳过子节点的解析操作
     */
    @Setter
    @Getter
    private boolean skipChildWhenEmpty;

    /**
     * 当前参数私有的参数定义,这些参数定义,只能在当前参数的值提取器中使用
     */
    @Getter
    @Setter
    @Builder.Default
    private Map<String,Parameter> privateItems=new HashMap<>();

    /**
     * 在整个参数处理上下文中所用到的所有公开参数定义,此处不会直接序列化传递到其他端,而是会采用schema的方式进行传递
     * 并由接收端完成Parameter的构造
     */
    @Getter
    @Setter
    @JsonIgnore
    @Builder.Default
    private transient Map<String,Parameter> publicItems=new HashMap<>();
    /**
     * 在整个参数处理上下文中所用到的所有公开类型定义,此处不会直接序列化传递到其他端,而是会采用schema的方式进行传递
     */
    @Getter
    @Setter
    @JsonIgnore
    @Builder.Default
    private transient Map<String,TypeDefinition> publicTypeDefinitions=new HashMap<>();

    @Getter
    @Setter
    @JsonIgnore
    @Builder.Default
    private transient Map<String,ValueProviderDefinition> publicValueProviderDefinitions=new HashMap<>();

    /**
     * 当前参数的所有子节点,包括私有的和公开的
     */
    @Getter
    @Builder.Default
    private List<ParameterItem> sortedAllItems=new ArrayList<>();

    @Setter
    @Builder.Default
    private List<Parameter> items = new ArrayList<>();

    /**
     * 基于类型定义创建参数定义,该方法会递归的将所有的子类型定义添加到类型定义中
     * @param name 参数名称
     * @param typeDefinition 类型定义
     * @return 参数定义
     */
    public static Parameter create(String name,TypeDefinition typeDefinition){
        Parameter root = Parameter.builder().name(name).type(typeDefinition)
                .scope(typeDefinition.getScope())
                .build();
        // 递归的将所有的子类型定义添加到类型定义中
        typeDefinition.getAllItems().forEach((item)->{
            root.addItem(Parameter.create(item.getName(), item));
        });
        return root;
    }

    public TypeDefinition getType(){
        return Optional.ofNullable(this.type).orElseGet(()->{
            if (this.typeId!=null){
                this.type=this.publicTypeDefinitions.get(this.typeId);
                return this.type;
            }
            return  null;
        });
    }

    public void setType(TypeDefinition type){
        this.type=type;
        if (type!=null){
            this.typeId=type.getId();
            if (Scope.isPublic(type)){
                this.publicTypeDefinitions.put(type.getId(),type);
            }
        }
    }
    public List<Parameter> getItems(){
        return this.sortedAllItems.stream().map(item-> getItemParameter(item).orElseThrow()).toList();
    }

    public Optional<Parameter> get(final String id){
        return this.sortedAllItems.stream().filter(item->item.getItemId().equals(id)).findFirst().flatMap(this::getItemParameter);
    }

    public Parameter getMust(final String id){
        return this.get(id).orElseThrow(()->new RuntimeException("can not find parameter by id "+id));
    }

    public Parameter addItem(Parameter parameter){
        if (parameter==null){
            return this;
        }
        if (Scope.isPublic(parameter.scope)){
            publicItems.put(parameter.getId(),parameter);
        }else {
            privateItems.put(parameter.getId(),parameter);
        }
        this.sortedAllItems.add(ParameterItem.builder().itemId(parameter.getId()).name(parameter.getName()).build());
        return this;
    }
    private Optional<Parameter> getItemParameter(ParameterItem item){
        return getItemParameter(item.getItemId());
    }

    private Parameter getItemParameter(String id, Supplier<Parameter> creator){
        return getItemParameter(id).orElseGet(()->{
            Parameter newParameter = creator.get();
            if (Scope.isPublic(newParameter.scope)){
                publicItems.put(id,newParameter);
            }else {
                privateItems.put(id, newParameter);
            }
            return newParameter;
        });
    }

    public Parameter synchronize(){
        return this.synchronizePublicDefinition().synchronizePublicTypeDefinition();
    }
    public Parameter synchronizePublicDefinition(){
        return  this.populatePublicDefinition(new HashMap<>());
    }

    public Parameter synchronizePublicTypeDefinition(){
        return this.populatePublicTypeDefinition(new HashMap<>());
    }

    private Parameter populatePublicTypeDefinition(Map<String,TypeDefinition> publicTypeDefinitions){
       if (this.publicTypeDefinitions.size()>0){
        this.publicTypeDefinitions.forEach((key,value)->{
            if (publicTypeDefinitions.containsKey(key)){
                TypeDefinition publicTypeDefinition = publicTypeDefinitions.get(key);
                if (!publicTypeDefinition.equals(value)){
                    log.warn("find a duplicate public type definition, it should never happened. id is {}, name is {}",key,value.getName());
                }
            }else{
                publicTypeDefinitions.put(key,value);
            }
           });
       }
       this.publicTypeDefinitions=publicTypeDefinitions;
         this.privateItems.forEach((key,value)->{
              value.populatePublicTypeDefinition(publicTypeDefinitions);
         });
         return this;
    }
    private Parameter populatePublicDefinition(Map<String,Parameter> publicItems){
        if (this.publicItems.size()>0){
            // 如果当前参数存在公开定义,则尝试将公开定义填充到当前参数中
            this.publicItems.forEach((key,value)->{
                if (publicItems.containsKey(key)){
                    Parameter publicItem = publicItems.get(key);
                    if (!publicItem.equals(value)){
                        log.warn("find a duplicate public item, it should never happened. id is {}, name is {}",key,value.getName());
                    }
                }else{
                    publicItems.put(key,value);
                }
            });
        }
        // 覆盖
        this.publicItems=publicItems;
        // 然后填充privateItems
        this.privateItems.forEach((key,value)->{
            value.populatePublicDefinition(publicItems);
        });
        this.publicItems.forEach((key,value)->{
            if (value.publicItems.equals(this.publicItems)){
                return;
            }
            value.populatePublicDefinition(publicItems);
        });
        return this;
    }
    /**
     * 尝试读取子参数定义
     * @param id 子参数定义的id
     * @return 子参数定义
     */
    private Optional<Parameter> getItemParameter(String id){
        if (privateItems.containsKey(id)){
            return Optional.of(privateItems.get(id));
        }else if (publicItems.containsKey(id)) {
            return Optional.of(publicItems.get(id));
        }
        return Optional.empty();
    }
}
