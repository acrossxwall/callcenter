package cc.efit.dialogue.biz.service;

import cc.efit.dialogue.api.enums.TemplateIntentionClassifyEnum;
import cc.efit.dialogue.api.enums.TemplateIntentionLevelTypeEnum;
import cc.efit.dialogue.api.enums.TemplateIntentionTypeEnum;
import cc.efit.dialogue.api.enums.TemplateVerbalEnum;
import cc.efit.dialogue.biz.domain.TemplateFlow;
import cc.efit.dialogue.biz.domain.TemplateVerbal;
import cc.efit.dialogue.biz.vo.node.NodeIntention;
import cc.efit.dialogue.api.vo.verbal.TemplateVerbalContentVo;

import java.util.List;

public interface TemplateDefaultService {

    TemplateFlow buildDefaultStartTemplateFlow(Integer callTemplateId);

    void buildDefaultTemplateIntentionLevel(Integer callTemplateId);
    void buildTemplateIntentionLevel(Integer callTemplateId, String name, String desc, TemplateIntentionLevelTypeEnum type, Integer sort);

    NodeIntention getNodeIntention(Integer callTemplateId, Integer flowId,
                                   String intentionName, TemplateIntentionTypeEnum type, Integer sort,
                                   TemplateIntentionClassifyEnum classify) ;

    NodeIntention getNodeIntention(Integer branchId,
                                   String intentionName) ;

    Integer buildFlowIntentionBranch(Integer callTemplateId, Integer flowId, Integer intentionId,Integer sort);

    Integer buildTemplateIntention(Integer id,
                               String intentionName, TemplateIntentionTypeEnum type, Integer sort,
                               TemplateIntentionClassifyEnum classifyEnum);

    Integer buildTemplateVerbal(Integer callTemplateId, String verbalName, String verbalContent, Integer source  ) ;
    Integer buildTemplateVerbal(Integer callTemplateId, String verbalName, String verbalContent, Integer source, TemplateVerbalEnum.Status uploadStatus,
                                String verbalPath);
    String getTemplateVerbalByIds(Integer callTemplateId,List<Integer> ids) ;

    List<TemplateVerbal> getTemplateVerbalListByIds(Integer callTemplateId, List<Integer> ids) ;

    void buildTemplateGlobalInteraction(Integer callTemplateId) ;

    void buildTemplateGlobalNoReply(Integer callTemplateId) ;

    void buildTemplateGlobalTts(Integer callTemplateId) ;

    String handlerSaveLocalTempFileToTemplate(Integer orgId, Integer callTemplateId, Integer fileId,String basePath,String verbalPath);

    List<Integer> buildTemplateFlowVerbal(Integer orgId, Integer callTemplateId,
                                          List<Integer> verbalIds, String name,
                                          List<TemplateVerbalContentVo> webContents,
                                          TemplateVerbalEnum.Source source,
                                          String basePath, String templatePath);
}
