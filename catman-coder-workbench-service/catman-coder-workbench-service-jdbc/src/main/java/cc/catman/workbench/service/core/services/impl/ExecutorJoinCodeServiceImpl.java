package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.common.IPStrategy;
import cc.catman.coder.workbench.core.executor.EJoinCodeRepeatStrategy;
import cc.catman.coder.workbench.core.executor.ExecutorJoinCode;
import cc.catman.coder.workbench.core.executor.ExecutorJoinCodeStatus;
import cc.catman.workbench.common.VPageHelper;
import cc.catman.workbench.service.core.common.page.PageParam;
import cc.catman.workbench.service.core.common.page.VPage;
import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.po.executor.joincode.ExecutorJoinCodeRef;
import cc.catman.workbench.service.core.po.executor.joincode.IPStrategyRef;
import cc.catman.workbench.service.core.po.executor.joincode.QExecutorJoinCodeRef;
import cc.catman.workbench.service.core.repossitory.executor.joincode.IExecutorJoinCodeRefRepository;
import cc.catman.workbench.service.core.repossitory.executor.joincode.IIPStrategyRepository;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.services.IExecutorJoinCodeService;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExecutorJoinCodeServiceImpl implements IExecutorJoinCodeService {
    @Resource
    private ModelMapper modelMapper;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private IBaseService baseService;

    @Resource
    private IExecutorJoinCodeRefRepository executorJoinCodeRefRepository;

    @Resource
    private IIPStrategyRepository ipStrategyRepository;

    @Override
    public Optional<ExecutorJoinCode> findById(String id) {
        return this.executorJoinCodeRefRepository.findById(id).map(this::convertToExecutorJoinCode);
    }

    @Override
    public void fuzzy() {

    }

    @Override
    public Optional<ExecutorJoinCode> findByJoinCode(String joinCode) {
        return this.executorJoinCodeRefRepository.findOne(Example.of(ExecutorJoinCodeRef.builder().code(joinCode)
                .build())).map(this::convertToExecutorJoinCode);
    }

    @Override
    public VPage<ExecutorJoinCode> fuzzy(PageParam page, String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return this.page(page);
        }
        keyword = "%" + keyword + "%";
        return VPageHelper.with(this.executorJoinCodeRefRepository
                .findAll(QExecutorJoinCodeRef.executorJoinCodeRef.id.like(keyword)
                                .or(QExecutorJoinCodeRef.executorJoinCodeRef.name.like(keyword))
                                .or(QExecutorJoinCodeRef.executorJoinCodeRef.code.like(keyword))
                                .or(QExecutorJoinCodeRef.executorJoinCodeRef.key.like(keyword))
                        ,
                        VPageHelper.with(page, Sort.by(Sort.Order.desc("createAt")))
                )).convert(executorJoinCodeRefs -> executorJoinCodeRefs.stream().map(this::convertToExecutorJoinCode).toList());
    }

    @Override
    public VPage<ExecutorJoinCode> fuzzy(PageParam page, String name, String code, String key, List<ExecutorJoinCodeStatus> states) {
        if (Objects.isNull(name) && Objects.isNull(code) && Objects.isNull(key) && Objects.isNull(states)) {
            return this.page(page);
        }

        Specification<ExecutorJoinCodeRef> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(name)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (Objects.nonNull(code)) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
            }
            if (Objects.nonNull(key)) {
                predicates.add(criteriaBuilder.like(root.get("key"), "%" + key + "%"));
            }
            if (Objects.nonNull(states)) {
                predicates.add(criteriaBuilder.in(root.get("status")).value(states.stream().map(Enum::name).toList()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return VPageHelper.with(this.executorJoinCodeRefRepository
                .findAll(specification
                        , VPageHelper.with(page, Sort.by(Sort.Order.desc("createAt")))
                )).convert(executorJoinCodeRefs -> executorJoinCodeRefs.stream().map(this::convertToExecutorJoinCode).toList());
    }

    @Override
    public boolean invalidJoinCode(String joinCode,String belongId) {
        List<ExecutorJoinCodeRef> all = this.executorJoinCodeRefRepository.findAll(Example.of(ExecutorJoinCodeRef.builder().code(joinCode).build()));
        if (all.isEmpty()) {
            return false;
        }
        if (all.size()>1){
            return true;
        }
        ExecutorJoinCodeRef executorJoinCodeRef = all.get(0);
        if(executorJoinCodeRef.getId().equals(belongId)){
            return false;
        }
        return true;
    }

    @Override
    public VPage<ExecutorJoinCode> page(PageParam page) {
        return VPageHelper.with(this.executorJoinCodeRefRepository
                .findAll(VPageHelper.with(page, Sort.by(Sort.Order.desc("createAt")))
                )).convert(executorJoinCodeRefs -> executorJoinCodeRefs.stream().map(this::convertToExecutorJoinCode).toList());
    }

    @Override
    public String createJoinCode() {
        return this.idGenerator.generateId().toString();
    }

    @Override
    @Transactional
    public ExecutorJoinCode createExecutorJoinCode() {
        return this.save(ExecutorJoinCode.builder()
                .code(this.createJoinCode())
                .kind("executor")
                .supportedKinds(List.of("executor"))
                .key(this.idGenerator.generateId().toString())
                .repeatStrategy(EJoinCodeRepeatStrategy.DENY)
                .status(ExecutorJoinCodeStatus.WAIT_ACTIVE)
                .build());
    }

    @Override
    public List<ExecutorJoinCode> findAll(String keyword) {
        if (Optional.ofNullable(keyword).filter(StringUtils::hasText).isEmpty()) {
            return this.executorJoinCodeRefRepository.findAll(
                    Sort.by(Sort.Order.by("disabled"), Sort.Order.desc("createAt"))
            ).stream().map(this::convertToExecutorJoinCode).toList();
        }
        Specification<ExecutorJoinCodeRef> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(root.get("id"), "%" + keyword + "%"));
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
            predicates.add(criteriaBuilder.like(root.get("code"), "%" + keyword + "%"));
            predicates.add(criteriaBuilder.like(root.get("key"), "%" + keyword + "%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return this.executorJoinCodeRefRepository.findAll(specification).stream().map(this::convertToExecutorJoinCode).toList();
    }

    @Override
    @Transactional
    public ExecutorJoinCode save(ExecutorJoinCode executorJoinCode) {
        if (Objects.isNull(executorJoinCode)) {
            return null;
        }

        if (invalidJoinCode(executorJoinCode.getCode(),executorJoinCode.getId())) {
            throw new RuntimeException("接入码已存在");
        }

        if (Objects.isNull(executorJoinCode.getSupportedKinds())
            || executorJoinCode.getSupportedKinds().isEmpty()
        ) {
            throw new RuntimeException("接入码支持的类型不能为空");
        }

        // 验证IP策略
        Optional.ofNullable(executorJoinCode.getIpFilter())
                .flatMap(ipStrategies -> ipStrategies
                        .stream()
                        .filter(ipStrategy -> !ipStrategy.isValidate())
                        .findAny())
                .ifPresent(ipStrategy -> {
                    throw new RuntimeException("IP策略不合法,请检查:" + ipStrategy.getIp());
                });

        ExecutorJoinCodeRef ref = this.modelMapper.map(executorJoinCode, ExecutorJoinCodeRef.class);

        if (Objects.isNull(ref.getKey())) {
            ref.setKey(this.idGenerator.generateId().toString());
        }

        if (Objects.isNull(ref.getCode())) {
            ref.setCode(this.createJoinCode());
        }

        ref.setSupportedKinds(String.join(",", executorJoinCode.getSupportedKinds()));

        ExecutorJoinCodeRef save = this.executorJoinCodeRefRepository.save(ref);

        // 批量更新IP策略
        List<IPStrategyRef> oldIps = this.ipStrategyRepository.findAll(Example.of(IPStrategyRef.builder().belongId(save.getId()).build()));
        Optional.ofNullable(oldIps).filter(oips -> !oips.isEmpty()).ifPresent(ipStrategies -> {
            this.ipStrategyRepository.deleteAllInBatch(oldIps);
        });
        AtomicInteger sort = new AtomicInteger(0);
        Optional.ofNullable(executorJoinCode.getIpFilter()).ifPresent(ipStrategies -> {
            ipStrategies.stream().map(ipStrategy -> {
                IPStrategyRef ipStrategyRef = this.modelMapper.map(ipStrategy, IPStrategyRef.class);
                ipStrategyRef.setId(null);
                ipStrategyRef.setBelongId(save.getId());
                ipStrategyRef.setSort(sort.getAndIncrement());
                return ipStrategyRef;
            }).forEach(ipStrategyRef -> this.ipStrategyRepository.save(ipStrategyRef));
        });
        // 保存base
        this.baseService.save(modelMapper.map(executorJoinCode, Base.class), ExecutorJoinCode.class.getSimpleName(), save.getId());
        return this.convertToExecutorJoinCode(save);
    }

    @Override
    public ExecutorJoinCode copyAndNoState(ExecutorJoinCode executorJoinCode) {
        if (Objects.isNull(executorJoinCode)) {
            return null;
        }
        ExecutorJoinCode ejc = this.modelMapper.map(executorJoinCode, ExecutorJoinCode.class);
        ejc.setId(null);
        ejc.setIpFilter(ejc.getIpFilter()
                .stream()
                .peek(ipStrategy -> ipStrategy.setId(null))
                .collect(Collectors.toList()));
        // 处理key
        if (Optional.ofNullable(ejc.getKey()).isEmpty()) {
            ejc.setKey(executorJoinCode.getId());
        }
        return ejc;
    }

    @Override
    @Transactional
    public ExecutorJoinCode flushJoinCode(ExecutorJoinCode executorJoinCode, String newCode, boolean oldKeepValid,
                                          String invalidReason) {
        if (Objects.isNull(executorJoinCode)) {
            return null;
        }
        if (Objects.isNull(newCode)) {
            newCode = this.createJoinCode();
        }
        if (invalidJoinCode(newCode,"")) {
            throw new RuntimeException("接入码已存在");
        }
        // 旧的接入码保持有效,则旧的记录不会被更改状态,新的接入码会被创建
        ExecutorJoinCode newEjc = this.modelMapper.map(executorJoinCode, ExecutorJoinCode.class);
        newEjc.setCode(newCode);
        newEjc.setId(null);
        newEjc.setInvalid(false);
        newEjc.setInvalidReason("");
        newEjc.setIpFilter(newEjc.getIpFilter().stream().peek(ipStrategy -> ipStrategy.setId(null)).collect(Collectors.toList()));
        // 旧的接入码失效,则旧的记录会被更改状态,新的接入码会被创建
        if (oldKeepValid) {
            executorJoinCode.setInvalid(true);
            executorJoinCode.setInvalidReason("刷新接入码");
            this.save(executorJoinCode);
        }
        return this.save(newEjc);
    }

    @Override
    public void deleteById(String id) {
        this.executorJoinCodeRefRepository.deleteById(id);
    }

    @Override
    public void deleteByJoinCode(String joinCode) {
        this.executorJoinCodeRefRepository.delete(ExecutorJoinCodeRef.builder().code(joinCode).build());
    }

    protected ExecutorJoinCode convertToExecutorJoinCode(ExecutorJoinCodeRef executorJoinCodeRef) {
        if (Objects.isNull(executorJoinCodeRef)) {
            return null;
        }
        ExecutorJoinCode ejc = this.modelMapper.map(executorJoinCodeRef, ExecutorJoinCode.class);

        List<IPStrategy> ipStrategies = this.ipStrategyRepository.findAll(Example.of(IPStrategyRef.builder().belongId(executorJoinCodeRef.getId()).build()))
                .stream()
                .sorted(Comparator.comparingInt(IPStrategyRef::getSort))
                .map(ipStrategyRef -> this.modelMapper.map(ipStrategyRef, IPStrategy.class))
                .collect(Collectors.toList());
        ejc.setIpFilter(ipStrategies);

        Optional.ofNullable(executorJoinCodeRef.getSupportedKinds())
                .ifPresent(supportedKinds -> {
                    if (supportedKinds.contains(",")) {
                        ejc.setSupportedKinds(Arrays.asList(supportedKinds.split(",")));
                    } else {
                        ejc.setSupportedKinds(List.of(supportedKinds));
                    }
                });

        return Optional.ofNullable(baseService.findByKindAndBelongId(ExecutorJoinCode.class.getSimpleName(), ejc.getId()))
                .map(base -> base.mergeInto(ejc))
                .orElse(ejc);
    }
}
