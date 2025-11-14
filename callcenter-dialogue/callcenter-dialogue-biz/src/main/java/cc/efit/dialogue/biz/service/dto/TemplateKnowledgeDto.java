package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.List;

/**
 * 知识库对象 efit_template_knowledge
 * 
 * @author across
 * @date 2025-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateKnowledgeDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 名称 */
    private String name;

    /** 1:业务问题,2:一般问题 */
    private Integer type;

    /** 意图id */
    private Integer intentionId;

    /** 话术ids */
    private List<Integer> verbalIds;

    /** 动作 */
    private Integer action;

    /** 目标节点id */
    private Integer targetFlowId;

    /** 允许被打断 1:是 0:否 */
    private Integer enableInterrupt;

    /** 短信模板id */
    private Integer smsTemplateId;

    /** 坐席组id */
    private Integer agentGroupId;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 电话号码 1 坐席组2 */
    private Integer transferType;

    /** 转人工需要的号码 */
    private String transferNumber;

    /** 客户标签 */
    private String label;

    /** 创建人用户id */
    private Integer userId;

    /** 部门id */
    private Integer deptId;

    private Integer orgId;

}
