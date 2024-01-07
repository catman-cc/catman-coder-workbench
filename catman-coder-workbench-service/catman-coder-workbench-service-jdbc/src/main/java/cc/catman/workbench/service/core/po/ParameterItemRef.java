package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParameterItemRef {
    @javax.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
    private String id ;

    private String belongParameterId;
    private String referencedParameterId;

    private int orderIndex;
}
