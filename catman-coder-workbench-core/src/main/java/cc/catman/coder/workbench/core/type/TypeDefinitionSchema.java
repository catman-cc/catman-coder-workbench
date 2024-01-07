package cc.catman.coder.workbench.core.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeDefinitionSchema {

    private String root;

    @Builder.Default
    private Map<String,TypeDefinition> definitions = new HashMap<>();
    @Builder.Default
    private Map<String, List<String>> refs = new HashMap<>();

    public List<String> getAllReference(String id) {
        return this.refs.entrySet().stream()
                .filter(e -> e.getValue().contains(id))
                .map(Map.Entry::getKey).toList();
    }
}
