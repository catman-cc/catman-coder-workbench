package cc.catman.coder.workbench.core.core.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cc.catman.coder.workbench.core.core.Base;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.core.value.Value;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 参数定义,每个参数定义都需要对应一个类型定义数据
 * 
 * 参数需要包含对应的值,值的数据结构要包含参数定义的结构,
 * 同时参数需要包含一条引用,该引用指向一个处理器,如果没有对应的处理器,
 * 那么参数将会尝试向父结构申请同名值
 * 因为多层结构的存在,向父结构申请同名值的时候,需要由内向外申请
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "parameter")
public class Parameter extends Base {

        @Id
        @Builder.Default
        private String id = UUID.randomUUID().toString();

        private String name;

        /**
         * 参数的简短描述
         */
        protected String describe;

        @Cascade(when = {
                        "#value.scope==T(cc.catman.coder.core.common.Scope).PUBLIC"
        })
        private TypeDefinition type;

        private Value value;

        private Value defaultValue;

        @Cascade(when = {
                        "#each.scope instanceof T(cc.catman.coder.core.common.Scope)",
                        "#each.scope==T(cc.catman.coder.core.common.Scope).PUBLIC"
        })
        @Builder.Default
        private List<Parameter> items = new ArrayList<>();

}
