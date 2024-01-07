package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ValueProviderParameterRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderParameterRefRepository extends JpaRepository<ValueProviderParameterRef,String> {
}
