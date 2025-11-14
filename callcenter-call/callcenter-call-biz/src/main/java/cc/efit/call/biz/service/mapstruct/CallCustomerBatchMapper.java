package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.biz.domain.CallCustomerBatch;
import cc.efit.call.biz.service.dto.CallCustomerBatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-09-10
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallCustomerBatchMapper extends BaseMapper<CallCustomerBatchDto, CallCustomerBatch> {

}