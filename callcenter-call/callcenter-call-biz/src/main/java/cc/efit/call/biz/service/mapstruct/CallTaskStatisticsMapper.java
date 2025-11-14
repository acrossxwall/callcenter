package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.api.domain.CallTaskStatistics;
import cc.efit.call.biz.service.dto.CallTaskStatisticsDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-10-15
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallTaskStatisticsMapper extends BaseMapper<CallTaskStatisticsDto, CallTaskStatistics> {

}