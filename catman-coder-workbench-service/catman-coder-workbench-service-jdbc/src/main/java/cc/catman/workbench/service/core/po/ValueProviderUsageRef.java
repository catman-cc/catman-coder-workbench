package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueProviderUsageRef {

    @javax.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
    private String id;
    /**
     * preprocessor, postprocessor, extractor
     */
    private String kind;

    private String valueProviderId;

    private String referencedValueProviderId;

    private int orderIndex;
}
