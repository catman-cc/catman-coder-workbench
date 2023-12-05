package cc.catman.workbench.service.core.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResourceDetails extends Resource{
    private Object details;
}
