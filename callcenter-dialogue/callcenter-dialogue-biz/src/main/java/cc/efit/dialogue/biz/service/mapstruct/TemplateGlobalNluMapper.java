package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateGlobalNlu;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNluDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-11-10
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateGlobalNluMapper extends BaseMapper<TemplateGlobalNluDto, TemplateGlobalNlu> {

}