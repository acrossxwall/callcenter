package cc.efit.call.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.call.api.domain.Line;
import cc.efit.call.biz.service.dto.LineDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-27
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LineMapper extends BaseMapper<LineDto, Line> {

}