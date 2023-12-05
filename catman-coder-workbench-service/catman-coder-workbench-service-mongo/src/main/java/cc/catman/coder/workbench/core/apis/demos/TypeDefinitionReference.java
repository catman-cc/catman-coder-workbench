package cc.catman.coder.workbench.core.apis.demos;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
public class TypeDefinitionReference {
    @Id
    private String id;
    private List<String> items;
}
