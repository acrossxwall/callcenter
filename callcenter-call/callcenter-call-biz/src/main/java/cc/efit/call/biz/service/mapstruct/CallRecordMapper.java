package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.api.domain.CallRecord;
import cc.efit.call.biz.service.dto.CallRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-09-26
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallRecordMapper extends BaseMapper<CallRecordDto, CallRecord> {

}