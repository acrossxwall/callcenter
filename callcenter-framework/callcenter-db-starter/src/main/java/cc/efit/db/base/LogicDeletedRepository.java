package cc.efit.db.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * 逻辑删除接口，继承本操作的id接口必须有deleted字段
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface LogicDeletedRepository<T,ID> extends JpaRepository<T, ID> {

    @Query("UPDATE #{#entityName} e SET e.deleted = 1 WHERE e.id = ?1")
    @Modifying
    @Transactional
    void logicDeleteById(ID id);

}
