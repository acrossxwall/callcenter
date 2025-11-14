package cc.efit.dialogue.biz.vo.node;

import java.util.List;

/**
 * nodes: [
 *       {
 *         id: "1",
 *         type: "html-card",
 *         x: 540,
 *         y: 100,
 *         properties: {
 *           title: "开场白",
 *           content: "话术",
 *           intentions: [
 *             { id: "1", name: "肯定" },
 *             { id: "2", name: "否定" },
 *             { id: "3", name: "默认" }
 *           ]
 *         }
 *       }]
 * @author across
 * @Description
 * @Date 2025-08-10 9:58
 */

public record TemplateNodeVo(Integer callTemplateId,  List<NodeInfo> nodes, List<EdgeInfo> edges) {
}
