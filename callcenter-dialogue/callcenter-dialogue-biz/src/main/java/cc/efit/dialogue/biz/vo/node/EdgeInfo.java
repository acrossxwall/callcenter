package cc.efit.dialogue.biz.vo.node;

import java.util.List;

public record EdgeInfo(
     String id,
     String type,
     Integer sourceNodeId,
     Integer targetNodeId,
     Integer sourceAnchorId,
     String targetAnchorId,
     EdgePoint startPoint,
     EdgePoint endPoint,
     List<EdgePoint> pointsList
) {}
