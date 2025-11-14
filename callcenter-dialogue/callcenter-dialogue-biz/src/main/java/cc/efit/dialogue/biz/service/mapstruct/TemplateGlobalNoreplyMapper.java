package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateGlobalNoreply;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNoreplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-21
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateGlobalNoreplyMapper extends BaseMapper<TemplateGlobalNoreplyDto, TemplateGlobalNoreply> {

}