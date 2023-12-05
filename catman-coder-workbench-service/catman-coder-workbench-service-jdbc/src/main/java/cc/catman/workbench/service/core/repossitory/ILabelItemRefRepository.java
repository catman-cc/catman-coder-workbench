package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.LabelItemRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILabelItemRefRepository extends JpaRepository<LabelItemRef,String> {
}
