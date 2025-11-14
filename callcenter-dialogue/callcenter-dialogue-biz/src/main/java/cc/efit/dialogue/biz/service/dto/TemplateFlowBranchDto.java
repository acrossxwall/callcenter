package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 节点意图分支对象 efit_template_flow_branch
 * 
 * @author across
 * @date 2025-08-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateFlowBranchDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 部门id */
    private Integer deptId;

    /** 用户id */
    private Integer userId;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 节点id */
    private Integer flowId;

    /** 意图id */
    private Integer intentionId;

    /** 目标flow id */
    private Integer targetFlowId;

    private Integer orgId;

}
