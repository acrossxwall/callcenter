package cc.efit.dialogue.biz.service.mapstruct;

import cc.efit.db.base.BaseMapper;
import cc.efit.dialogue.biz.domain.TemplateFlowBranch;
import cc.efit.dialogue.biz.service.dto.TemplateFlowBranchDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
* @author across
* @date 2025-08-15
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateFlowBranchMapper extends BaseMapper<TemplateFlowBranchDto, TemplateFlowBranch> {

}