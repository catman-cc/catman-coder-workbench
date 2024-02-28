package cc.catman.coder.workbench.core.apis.service;

import cc.catman.coder.workbench.core.apis.repositories.ConfigurationItem;
import cc.catman.coder.workbench.core.apis.repositories.ConfigurationItemsRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfigurationItemsServiceImpl implements ConfigurationItemsService {
    @Resource
    private ConfigurationItemsRepository configurationItemsRepository;
    @Override
    public Optional<ConfigurationItem> findByName(String name) {
        return configurationItemsRepository.findOne(Example.of(ConfigurationItem.builder().name(name).build()));
    }

    @Override
    public ConfigurationItem save(ConfigurationItem item) {
        if (Optional.ofNullable(item.getId()).filter(StringUtils::hasText).isEmpty()){
            item.setId(UUID.randomUUID().toString());
        }
        return configurationItemsRepository.save(item);
    }
}
