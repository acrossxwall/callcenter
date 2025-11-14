package cc.efit.process.api;


import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;

import java.util.List;
import java.util.Map;

/**
 * 拨打中聊天记录处理的api
 */
public interface ChatProcessVoipApi {

    ChatProcessRes chatProcess(ChatProcessReq req);

    void initTemplateInfoToLocalCache(Integer callTemplateId, Map<String, List<TemplateNodeBranchInfo>> branchMap,
                                      Map<String,String > intentionMap,
                                      Map<String, TemplateKnowledgeVo> knowledgeVoMap);
}
