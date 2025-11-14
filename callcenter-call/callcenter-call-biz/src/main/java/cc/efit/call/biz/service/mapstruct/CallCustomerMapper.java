package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.api.domain.CallCustomer;
import cc.efit.call.biz.service.dto.CallCustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-09-10
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallCustomerMapper extends BaseMapper<CallCustomerDto, CallCustomer> {

}