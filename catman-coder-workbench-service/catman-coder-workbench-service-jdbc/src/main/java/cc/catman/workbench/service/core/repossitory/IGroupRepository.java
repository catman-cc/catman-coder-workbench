package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.GroupRef;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGroupRepository extends PagingAndSortingRepository<GroupRef,String> {

}
