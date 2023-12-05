package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.BaseRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBaseRefRepository extends JpaRepository<BaseRef,String> {
}
