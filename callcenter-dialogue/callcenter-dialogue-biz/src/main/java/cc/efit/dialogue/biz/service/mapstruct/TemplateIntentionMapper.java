package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateIntention;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-14
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateIntentionMapper extends BaseMapper<TemplateIntentionDto, TemplateIntention> {

}