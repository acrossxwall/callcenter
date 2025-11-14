package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateWordsCategory;
import cc.efit.dialogue.biz.service.dto.TemplateWordsCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-19
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateWordsCategoryMapper extends BaseMapper<TemplateWordsCategoryDto, TemplateWordsCategory> {

}