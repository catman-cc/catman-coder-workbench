package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContextVariable {
    private Field field;
    private Class<?> type;

    private Object root;
    private Object value;

    private Cascade cascade;

    private CascadeType supportType;

    private Object each;

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("root",this.root);
        map.put("value",this.value);
        map.put("cascade",this.cascade);
        map.put("supportType",this.supportType);
        map.put("each",this.each);
        map.put("field",this.field);
        map.put("type",this.type);
        return map;
    }
}

