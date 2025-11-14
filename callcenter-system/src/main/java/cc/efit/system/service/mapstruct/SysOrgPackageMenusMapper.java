package cc.efit.system.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.system.service.dto.SysOrgPackageMenusDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-10-28
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysOrgPackageMenusMapper extends BaseMapper<SysOrgPackageMenusDto, SysOrgPackageMenus> {

}