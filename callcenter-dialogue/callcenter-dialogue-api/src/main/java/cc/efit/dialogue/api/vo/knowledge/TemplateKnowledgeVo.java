package cc.efit.dialogue.api.vo.knowledge;

import cc.efit.dialogue.api.vo.verbal.TemplateVerbalContentVo;
import lombok.Data;

import java.util.List;
@Data
public class TemplateKnowledgeVo {
    private Integer id;
    /** 名称 */
    private String name;
    /** 1:业务问题,2:一般问题 */
    private Integer type;
    /** 意图id */
    private Integer intentionId;
    /** 动作 1.回到跳出主流程节点 2.回到跳出主流程的上个节点 3.指定节点 4,转人工 5.挂断 */
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
    /** 转人工需要的手机号 */
    private String transferNumber;
    /** 客户标签 */
    private String label;
    private List<TemplateVerbalContentVo> verbalList;

    private Integer orgId;
}
