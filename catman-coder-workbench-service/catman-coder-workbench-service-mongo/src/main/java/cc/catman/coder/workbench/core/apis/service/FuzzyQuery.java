package cc.catman.coder.workbench.core.apis.service;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
public class FuzzyQuery {

    private String key;

    private List<String> fields;

    public boolean isValid(){
        return StringUtils.hasText(key)&&fields.size()>0;
    }
    public Query toQuery(Class<?> entityClass) {
        return Query.query(
                Criteria.where("")
                        .orOperator(
                                fields.stream()
                                        .map(f -> Optional.ofNullable(ReflectionUtils.findField(entityClass, f))
                                                .map(field -> {
                                                    Class<?> type = field.getType();
                                                    if (Collection.class.isAssignableFrom(type) || type.isArray()) {
                                                        return Criteria.where(f)
                                                                .elemMatch(Criteria.where("$regex").is(key));
                                                    }
                                                    return Criteria.where(f).regex(key);
                                                })
                                                .orElse(null))
                                        .filter(Objects::nonNull)
                                        .toArray(Criteria[]::new)));
    }
}
