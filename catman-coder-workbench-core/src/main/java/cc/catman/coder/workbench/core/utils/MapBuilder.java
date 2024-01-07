package cc.catman.coder.workbench.core.utils;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {

    private Map<K, V> map;

    /**
     * copy map ,not reference ,new map
     * @param map source map
     * @return new map
     * @param <K> key
     * @param <V> value
     */
    public static <K, V> MapBuilder<K, V> copyOf(Map<K, V> map) {
        return new MapBuilder<>(new HashMap<>(map));
    }

    /**
     * use map ,reference map
     * @param map source map
     * @return new map
     * @param <K> key
     * @param <V> value
     */
    public static <K, V> MapBuilder<K, V> use(Map<K, V> map) {
        return new MapBuilder<>(map);
    }

    public static <K, V> MapBuilder<K, V> create() {
        return new MapBuilder<>();
    }
    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder() {
        this(new HashMap<>());
    }

    public MapBuilder<K, V> add(K key, V value) {
        map.put(key, value);
        return this;
    }
    public MapBuilder<K, V> put(K key, V value) {
        return this.add(key, value);
    }
    public Map<K, V> build() {
        return map;
    }
}
