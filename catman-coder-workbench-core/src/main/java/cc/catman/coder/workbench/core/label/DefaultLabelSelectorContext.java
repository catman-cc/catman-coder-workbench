package cc.catman.coder.workbench.core.label;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultLabelSelectorContext implements ILabelSelectorContext {
    private ObjectMapper objectMapper;
    private ILabelSelectorFactory labelSelectorFactory;
    public static DefaultLabelSelectorContext createDefault(){
        return DefaultLabelSelectorContext.builder()
                .objectMapper(new ObjectMapper())
                .labelSelectorFactory(DefaultLabelSelectorFactory.createDefault())
                .build();
    }
    @Override
    public boolean valid(ILabelSelector<?> selector, Object object) {
       return selector.valid(object,this);
    }

    @Override
    @SneakyThrows
    public boolean valid(String selectorJson, Object object) {
        JsonNode node = objectMapper.readTree(selectorJson);
        // 转换为ILabelSelector定义
        ILabelSelector<?> iLabelSelector = labelSelectorFactory.create(node);
        return this.valid(iLabelSelector,object);

    }

}
