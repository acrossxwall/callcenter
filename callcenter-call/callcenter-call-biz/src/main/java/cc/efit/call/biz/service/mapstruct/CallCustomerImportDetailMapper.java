package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.biz.domain.CallCustomerImportDetail;
import cc.efit.call.biz.service.dto.CallCustomerImportDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-09-12
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallCustomerImportDetailMapper extends BaseMapper<CallCustomerImportDetailDto, CallCustomerImportDetail> {

}