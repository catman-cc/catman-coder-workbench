package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.TagRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagRefRepository extends JpaRepository<TagRef,String> {

}
