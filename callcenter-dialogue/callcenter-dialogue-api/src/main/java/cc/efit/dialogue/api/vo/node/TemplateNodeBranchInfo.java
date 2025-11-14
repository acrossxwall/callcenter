package cc.efit.dialogue.api.vo.node;

public record TemplateNodeBranchInfo(
        /**
         * 节点id
         */
        Integer flowId,
        /**
         * 意图id
         */
        Integer intentionId,
        /**
         * 目标流程节点id
         */
        Integer targetFlowId,
        /**
         * 意图分支类型
         */
        Integer type,
        /**
         * 意图分支名称
         */
        String name,
        /**
         * 意图分类
         */
        Integer classify
) {}
