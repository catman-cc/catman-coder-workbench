package cc.catman.coder.workbench.core.apis.service;

import cc.catman.coder.workbench.core.apis.repositories.ConfigurationItem;

import java.util.Optional;

public interface ConfigurationItemsService {

    Optional<ConfigurationItem> findByName(String name);

    ConfigurationItem save( ConfigurationItem item);
}
