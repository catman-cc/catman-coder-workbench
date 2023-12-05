package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CascadeInfo {
    private String collection;
    private String id;
    private Class<?> _class;

    public Document toDocument() {
        return new Document()
                .append("collection", this.collection)
                .append("id", this.id)
                .append("_class", _class.getCanonicalName())
                ;
    }

    public static Optional<CascadeInfo> from(Document document) {
        CascadeInfo cascadeInfo = new CascadeInfo();
        Optional.ofNullable(document.getString("collection")).ifPresent(cascadeInfo::setCollection);
        Optional.ofNullable(document.getString("id")).ifPresent(cascadeInfo::setId);
        Optional.ofNullable(document.getString("_class")).ifPresent(cascadeInfo::set_class);
        return Optional.of(cascadeInfo);
    }

    public boolean isValid() {
        return Optional.ofNullable(this.id).isPresent()
               && Optional.ofNullable(this._class).isPresent();
    }

    private void set_class(String s) {
        try {
            this._class = Class.forName(s);
        } catch (ClassNotFoundException ignored) {
        }
    }
}
