package cc.catman.coder.workbench.core;

public interface JSONMapper {
    <T> T fromJson(String json, Class<T> clazz);

    String toJson(Object object);
}
