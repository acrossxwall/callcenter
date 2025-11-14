package cc.efit.dialogue.api.vo.template;

import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;

import java.util.List;
import java.util.Map;

public record TemplateInitInfo(
        /**
         * 模板id
         */
        Integer callTemplateId,
        /**
         * 节点分支信息
         */
        Map<String, List<TemplateNodeBranchInfo>> branchMap,
        /**
         * 意图信息
         */
        Map<String, String> intentionMap,
        /**
         * 知识库信息
         */
        Map<String, TemplateKnowledgeVo> knowledgeVoMap
) {
}
