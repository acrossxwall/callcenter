package cc.efit.system.repository;

import cc.efit.system.domain.SysOrg;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 多租户Repository接口
 * 
 * @author across
 * @date 2025-08-06
 */
public interface SysOrgRepository extends LogicDeletedRepository<SysOrg, Integer>, JpaSpecificationExecutor<SysOrg> {

    List<SysOrg> findAllByStatus(Integer status);

    @Query("UPDATE SysOrg s SET s.status = ?2 WHERE s.id = ?1")
    @Modifying
    @Transactional
    void updateOrgStatus(Integer id,Integer status);

    List<SysOrg> findByPackageIdAndStatus(Integer packageId,Integer status);

    SysOrg findByName(String name);
}
