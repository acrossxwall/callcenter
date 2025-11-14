package cc.efit.system.repository;

import cc.efit.system.domain.SysOrgPackage;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 租户套餐表Repository接口
 * 
 * @author across
 * @date 2025-10-28
 */
public interface SysOrgPackageRepository extends LogicDeletedRepository<SysOrgPackage, Integer>, JpaSpecificationExecutor<SysOrgPackage> {

    SysOrgPackage findByName(String name);

    List<SysOrgPackage> findByStatus(Integer status);
}
