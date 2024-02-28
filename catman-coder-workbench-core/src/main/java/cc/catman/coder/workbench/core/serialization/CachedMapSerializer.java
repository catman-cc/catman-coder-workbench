package cc.catman.coder.workbench.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CachedMapSerializer extends MapSerializer {
    private  Map<String,Integer> cache;

    public CachedMapSerializer(Set<String> ignoredEntries, Set<String> includedEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Map<String, Integer> cache) {
        super(ignoredEntries, includedEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, valueSerializer);
        this.cache = cache;
    }

    public CachedMapSerializer(Set<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Map<String, Integer> cache) {
        super(ignoredEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, valueSerializer);
        this.cache = cache;
    }

    public CachedMapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignoredEntries, Set<String> includedEntries, Map<String, Integer> cache) {
        super(src, property, keySerializer, valueSerializer, ignoredEntries, includedEntries);
        this.cache = cache;
    }

    public CachedMapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignoredEntries, Map<String, Integer> cache) {
        super(src, property, keySerializer, valueSerializer, ignoredEntries);
        this.cache = cache;
    }

    public CachedMapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue, boolean suppressNulls, Map<String, Integer> cache) {
        super(src, vts, suppressableValue, suppressNulls);
        this.cache = cache;
    }

    public CachedMapSerializer(MapSerializer src, Object filterId, boolean sortKeys, Map<String, Integer> cache) {
        super(src, filterId, sortKeys);
        this.cache = cache;
    }

    public CachedMapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue, Map<String, Integer> cache) {
        super(src, vts, suppressableValue);
        this.cache = cache;
    }

    protected CachedMapSerializer(Set<String> ignoredEntries, Set<String> includedEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer) {
        super(ignoredEntries, includedEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, valueSerializer);
    }

    protected CachedMapSerializer(Set<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer) {
        super(ignoredEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, valueSerializer);
    }

    protected CachedMapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignoredEntries, Set<String> includedEntries) {
        super(src, property, keySerializer, valueSerializer, ignoredEntries, includedEntries);
    }

    protected CachedMapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignoredEntries) {
        super(src, property, keySerializer, valueSerializer, ignoredEntries);
    }

    protected CachedMapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue, boolean suppressNulls) {
        super(src, vts, suppressableValue, suppressNulls);
    }

    protected CachedMapSerializer(MapSerializer src, Object filterId, boolean sortKeys) {
        super(src, filterId, sortKeys);
    }

    protected CachedMapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue) {
        super(src, vts, suppressableValue);
    }

    @Override
    public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Integer id = cache.get(value.getClass().getName());
        if (id == null) {
            id = System.identityHashCode(value);
            cache.put(value.getClass().getName(), id);
            super.serialize(value, gen, provider);
        } else {
            gen.writeStartObject();
            gen.writeFieldName("@id");
            gen.writeNumber(id);
            gen.writeEndObject();
        }
    }

    @Override
    public MapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignored, boolean sortKeys) {
      return new CachedMapSerializer(this, _valueTypeSerializer, _suppressableValue, _suppressNulls, cache);
    }

    @Override
    public MapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignored, Set<String> included, boolean sortKeys) {
        return new CachedMapSerializer(this, _valueTypeSerializer, _suppressableValue, _suppressNulls, cache);
    }
}
