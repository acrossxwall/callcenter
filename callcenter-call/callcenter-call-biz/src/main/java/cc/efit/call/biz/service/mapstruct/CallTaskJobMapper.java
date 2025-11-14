package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.biz.domain.CallTaskJob;
import cc.efit.call.biz.service.dto.CallTaskJobDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-10-20
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallTaskJobMapper extends BaseMapper<CallTaskJobDto, CallTaskJob> {

}