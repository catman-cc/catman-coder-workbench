package cc.catman.coder.workbench.core.value.template;

import cc.catman.coder.workbench.core.type.TypeDefinition;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DefaultValueProviderTemplateManager implements ValueProviderTemplateManager{

    @Builder.Default
    private List<ValueProviderTemplate> templates=new ArrayList<>();

    @Override
    public ValueProviderTemplateManager register(ValueProviderTemplate template) {
        // 注册新的模板
        templates.add(template);
        return this;
    }

    @Override
    public List<ValueProviderTemplate> search(String keyword) {
        RatcliffObershelp ratcliffObershelp = new RatcliffObershelp();
        return templates.stream().sorted((o1, o2) -> {
            double similarity1 = Double.max(ratcliffObershelp.similarity(o1.getName(), keyword),ratcliffObershelp.similarity(o1.getDescription(), keyword));
            double similarity2 = Double.max(ratcliffObershelp.similarity(o2.getName(), keyword),ratcliffObershelp.similarity(o2.getDescription(), keyword));
            return Double.compare(similarity2, similarity1);
        }).toList();
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByArgs(TypeDefinition args) {
        return templates.stream().filter(template -> {
            TypeDefinition need = template.getArgs();
            return args.getType().canConvert(need.getType());
        }).toList();
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByResult(TypeDefinition result) {
       return templates.stream().filter(template -> {
           TypeDefinition need = template.getResult();
           return result.getType().canConvert(need.getType());
       }).toList();
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByArgsAndResult(TypeDefinition args, TypeDefinition result) {
        return templates.stream().filter(template -> {
            TypeDefinition needArgs = template.getArgs();
            TypeDefinition needResult = template.getResult();
            return args.getType().canConvert(needArgs.getType()) && result.getType().canConvert(needResult.getType());
        }).toList();
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByKind(String kind) {
        return templates.stream().filter(template -> template.getKind().equals(kind)).toList();
    }

}
