package cc.catman.workbench.service.core.po.parameter;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParameterItemRef extends CommonRef {
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id ;

    private String belongParameterId;
    private String referencedParameterId;

    private int orderIndex;
}
