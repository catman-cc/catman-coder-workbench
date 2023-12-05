package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ValueProviderConfigItemRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderConfigItemRefRepository extends JpaRepository<ValueProviderConfigItemRef,String> {

}
