package cc.efit.dialogue.biz.vo.template;

import cc.efit.dialogue.biz.vo.node.EdgeInfo;
import cc.efit.dialogue.biz.vo.node.NodeInfo;

import java.util.List;

public record TemplateFlowVo(Integer callTemplateId, List<NodeInfo> nodes,List<EdgeInfo> edges) {
}
