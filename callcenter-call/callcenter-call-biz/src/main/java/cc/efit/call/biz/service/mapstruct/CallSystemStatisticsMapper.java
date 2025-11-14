package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.call.biz.service.dto.CallSystemStatisticsDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-10-22
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallSystemStatisticsMapper extends BaseMapper<CallSystemStatisticsDto, CallSystemStatistics> {

}