package cc.catman.workbench.service.core.repossitory.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * BaseRepository for all repository
 * @param <T> entity
 * @param <ID> id
 */
@NoRepositoryBean
public interface IBaseRepository<T,ID> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> , QuerydslPredicateExecutor<T> {
}
