package cc.catman.workbench.service.core.services;


import cc.catman.workbench.service.core.entity.Base;

import java.util.Optional;

/**
 * 用于获取通用基础数据
 */
public interface IBaseService {

    Base findBaseById(String id);

    Base findBaseByBelongId(String id);

    <T extends Base> Base save(Base base, String kind, String belongId);

    Base updateScopeByKindAndBelongID(String kind, String belongId, String scope);

    Optional<Base> deleteByKindAndBelongId(String kind, String belongId);
}
