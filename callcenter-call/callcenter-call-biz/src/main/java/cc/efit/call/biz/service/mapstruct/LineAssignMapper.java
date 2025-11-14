package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.api.domain.LineAssign;
import cc.efit.call.biz.service.dto.LineAssignDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-28
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LineAssignMapper extends BaseMapper<LineAssignDto, LineAssign> {

}