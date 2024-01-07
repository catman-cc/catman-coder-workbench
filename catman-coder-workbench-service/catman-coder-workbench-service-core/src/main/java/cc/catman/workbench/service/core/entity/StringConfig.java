package cc.catman.workbench.service.core.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringConfig {
    private String id;

    private String belongId;

    private String value;
}
