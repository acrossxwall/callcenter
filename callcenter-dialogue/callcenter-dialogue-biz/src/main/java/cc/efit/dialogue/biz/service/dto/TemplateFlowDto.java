package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.List;

/**
 * 流程节点对象 efit_template_flow
 * 
 * @author across
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateFlowDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 节点名称 */
    private String nodeName;

    /** 话术模板ID */
    private Integer callTemplateId;

    /** 是否挂断 */
    private Integer enableHangup;

    /** 挂机方式 */
    private Integer hangupMode;

    /** 转人工 */
    private Integer enableTransfer;

    /** 转接坐席的号码 */
    private String transferNumber;

    /** 转接方式 */
    private Integer transferType;

    /** 允许打断  */
    private Integer enableInterrupt;

    /** 跳转知识库  */
    private Integer enableMatchKnowledge;

    /** 匹配知识库 */
    private List<Integer> matchKnowledgeIds;
    /** 忽略用户回复 */
    private Integer enableIgnoreReply;
    /** 用户回复 */
    private Integer ignoreReplyType;
    /**  发送短信 0-否 1-是 */
    private Integer enableSendSms;
    /** 短信模板 */
    private Integer smsTemplateId;

    /** 标签 */
    private String nodeLabel;

    /** 1:开始节点 2:过程节点 3:IVR节点 4:信息采集节点 */
    private Integer nodeType;

    /** 话术ids */
    private List<Integer> verbalIds;

    /** 节点坐标 */
    private String coordinate;

    /** 用户id */
    private Integer userId;

    /** 转接坐席组 */
    private Integer transferAgentGroupId;

    /** 提取关键词 */
    private Integer triggerMatch;

    /** 关键词预测 */
    private Integer matchId;

    /** 部门id */
    private Integer deptId;

    private Integer orgId;
    /** 设置意向等级 */
    private String levelName;
}
