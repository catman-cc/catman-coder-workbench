package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.entity.StringConfig;
import cc.catman.workbench.service.core.po.StringConfigPO;
import cc.catman.workbench.service.core.repossitory.IStringConfigRefRepository;
import cc.catman.workbench.service.core.services.IStringConfigService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class StringConfigServiceImpl implements IStringConfigService {
    @Resource
    private IStringConfigRefRepository stringConfigRefRepository;
    @Resource
    private ModelMapper modelMapper;
    @Override
    public Optional<StringConfig> findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(i-> stringConfigRefRepository
                        .findById(i)
                        .map(p->modelMapper.map(p,StringConfig.class))
                );
    }

    @Override
    public Optional<StringConfig> find(StringConfig config) {
        StringConfigPO po=modelMapper.map(config,StringConfigPO.class);
        return stringConfigRefRepository.findOne(Example.of(po)).map(p->modelMapper.map(p,StringConfig.class));
    }

    @Override
    public StringConfig save(StringConfig config) {
        // 如果存在config存在id，则更新
        if(config.getId()!=null){
            Optional<StringConfig> sc = findById(config.getId());
            if(sc.isPresent()){
                StringConfigPO po=modelMapper.map(config,StringConfigPO.class);
                return modelMapper.map(stringConfigRefRepository.save(po),StringConfig.class);
            }
        }
        StringConfigPO po=modelMapper.map(config,StringConfigPO.class);
       return Optional.of(stringConfigRefRepository.save(po)).map(p->modelMapper.map(p,StringConfig.class)).orElse(null);
    }

    @Override
    public void delete(StringConfig config) {
        stringConfigRefRepository.deleteById(config.getId());
    }

    @Override
    public Optional<StringConfig> deleteById(String id) {
        Optional<StringConfig> byId = findById(id);
        if(byId.isPresent()){
            stringConfigRefRepository.deleteById(id);
            return byId;
        }
        return Optional.empty();
    }
}
