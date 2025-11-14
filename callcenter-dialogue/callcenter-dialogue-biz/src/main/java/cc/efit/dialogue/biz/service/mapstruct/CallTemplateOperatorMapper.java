package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.CallTemplateOperator;
import cc.efit.dialogue.biz.service.dto.CallTemplateOperatorDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-12
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallTemplateOperatorMapper extends BaseMapper<CallTemplateOperatorDto, CallTemplateOperator> {

}