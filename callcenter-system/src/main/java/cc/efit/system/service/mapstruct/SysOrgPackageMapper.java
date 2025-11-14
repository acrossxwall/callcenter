package cc.efit.system.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.system.domain.SysOrgPackage;
import cc.efit.system.service.dto.SysOrgPackageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-10-28
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysOrgPackageMapper extends BaseMapper<SysOrgPackageDto, SysOrgPackage> {

}