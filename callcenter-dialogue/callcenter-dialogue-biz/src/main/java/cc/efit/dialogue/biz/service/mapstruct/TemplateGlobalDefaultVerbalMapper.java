package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateGlobalDefaultVerbal;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalDefaultVerbalDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-11-11
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateGlobalDefaultVerbalMapper extends BaseMapper<TemplateGlobalDefaultVerbalDto, TemplateGlobalDefaultVerbal> {

}