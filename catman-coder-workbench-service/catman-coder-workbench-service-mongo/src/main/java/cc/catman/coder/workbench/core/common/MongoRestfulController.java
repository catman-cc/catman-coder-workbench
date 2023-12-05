package cc.catman.coder.workbench.core.common;

import cc.catman.coder.workbench.core.apis.service.FuzzyQuery;

import java.util.List;

/**
 * mongo资源的restful类型控制器
 */
public interface MongoRestfulController<T> {

    List<T> list();

    List<T> fuzzy(FuzzyQuery fuzzyQuery);

    T findById(String id);

    T save(T entity);
}
