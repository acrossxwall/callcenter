package cc.efit.system.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.system.domain.SysOrg;
import cc.efit.system.service.dto.SysOrgDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-06
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysOrgMapper extends BaseMapper<SysOrgDto, SysOrg> {

}