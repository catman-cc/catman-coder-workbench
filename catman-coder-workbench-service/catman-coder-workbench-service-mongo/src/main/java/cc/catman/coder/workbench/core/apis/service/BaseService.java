package cc.catman.coder.workbench.core.apis.service;

import java.util.List;

public interface BaseService<T,ID> {
    T save( T parameter);

    T findById(ID id);

    List< T> list( T entity);

    List< T> fuzzyQuery(FuzzyQuery fuzzyQuery);
}
