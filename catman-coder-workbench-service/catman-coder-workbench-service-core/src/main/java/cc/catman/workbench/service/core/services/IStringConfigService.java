package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.entity.StringConfig;

import java.util.Optional;

public interface IStringConfigService {
    Optional<StringConfig> findById(String id);
    Optional<StringConfig> find(StringConfig config);
    StringConfig save(StringConfig stringConfig);

    void delete(StringConfig stringConfig);

    Optional<StringConfig> deleteById(String id);
}
