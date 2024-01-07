package cc.catman.coder.workbench.core.value.report;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportMessage<T> {
    private String contextId;
    private String eventKind;
    private String sourceType;
    private String sourceId;
    private String batchId;
    private String channelId;
    private T data;
}
