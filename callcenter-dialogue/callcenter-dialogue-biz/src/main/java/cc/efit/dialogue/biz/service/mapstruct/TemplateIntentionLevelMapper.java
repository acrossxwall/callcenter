package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateIntentionLevel;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionLevelDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-13
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateIntentionLevelMapper extends BaseMapper<TemplateIntentionLevelDto, TemplateIntentionLevel> {

}