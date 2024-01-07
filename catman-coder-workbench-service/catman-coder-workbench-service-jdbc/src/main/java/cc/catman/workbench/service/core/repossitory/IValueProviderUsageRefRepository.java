package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ValueProviderUsageRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderUsageRefRepository extends JpaRepository<ValueProviderUsageRef,String> {
}
