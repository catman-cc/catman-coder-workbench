package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ValueProviderRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderRefRepository extends JpaRepository<ValueProviderRef,String> {
}
