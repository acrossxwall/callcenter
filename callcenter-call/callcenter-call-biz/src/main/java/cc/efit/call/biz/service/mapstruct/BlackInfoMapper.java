package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.biz.domain.BlackInfo;
import cc.efit.call.biz.service.dto.BlackInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-27
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BlackInfoMapper extends BaseMapper<BlackInfoDto, BlackInfo> {

}