package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ValueProviderDefinitionRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderDefinitionRefRepository extends JpaRepository<ValueProviderDefinitionRef,String> {
}
