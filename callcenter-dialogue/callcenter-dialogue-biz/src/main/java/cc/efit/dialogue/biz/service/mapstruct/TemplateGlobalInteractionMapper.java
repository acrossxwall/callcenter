package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateGlobalInteraction;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalInteractionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-21
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateGlobalInteractionMapper extends BaseMapper<TemplateGlobalInteractionDto, TemplateGlobalInteraction> {

}