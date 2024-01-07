package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.StringConfigPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStringConfigRefRepository extends JpaRepository<StringConfigPO,String> {
}
