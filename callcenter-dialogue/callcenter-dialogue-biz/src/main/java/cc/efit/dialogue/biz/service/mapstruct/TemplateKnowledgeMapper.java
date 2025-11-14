package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateKnowledge;
import cc.efit.dialogue.biz.service.dto.TemplateKnowledgeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-16
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateKnowledgeMapper extends BaseMapper<TemplateKnowledgeDto, TemplateKnowledge> {

}