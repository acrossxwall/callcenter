package cc.efit.system.repository;

import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * 租户套餐菜单关联表Repository接口
 * 
 * @author across
 * @date 2025-10-28
 */
public interface SysOrgPackageMenusRepository extends LogicDeletedRepository<SysOrgPackageMenus, Integer>, JpaSpecificationExecutor<SysOrgPackageMenus> {

    List<SysOrgPackageMenus> findByPackageId(Integer packageId);
    @Query("""
            update SysOrgPackageMenus set deleted=1  where packageId = ?1 and menuId in  ?2
            """)
    @Modifying
    void deleteByPackageIdAndMenuIdIn(Integer id, Set<Integer> delete);

    @Query("""
            select sopm.menuId from SysOrgPackageMenus sopm join SysOrgPackage sop on sopm.packageId = sop.id
            join SysOrg so on sop.id = so.packageId
             where so.id = ?1
            """)
    List<Integer> selectMenuIdByOrgId(Integer orgId);
}
