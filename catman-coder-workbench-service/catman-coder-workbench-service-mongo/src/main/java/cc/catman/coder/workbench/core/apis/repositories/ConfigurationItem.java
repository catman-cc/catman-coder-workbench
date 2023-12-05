package cc.catman.coder.workbench.core.apis.repositories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("configuration-items")
public class ConfigurationItem {
    @Id
    private String id;

    private String name;

    private Object info;
}
