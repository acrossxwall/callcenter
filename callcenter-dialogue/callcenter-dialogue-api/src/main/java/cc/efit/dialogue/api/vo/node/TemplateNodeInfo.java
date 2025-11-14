package cc.efit.dialogue.api.vo.node;

import cc.efit.dialogue.api.vo.verbal.TemplateVerbalContentVo;
import lombok.Data;

import java.util.List;
@Data
public class TemplateNodeInfo {

    /** 主键 */
    private Integer id;
    /** 节点名称 */
    private String nodeName;

    /** 话术模板ID */
    private Integer callTemplateId;

    /** 是否挂断 */
    private Integer enableHangup;

    /** 挂机方式 1-回复话术后挂机 0-直接挂机 */
    private Integer hangupMode;

    /** 转人工 */
    private Integer enableTransfer;

    /** 转接坐席的手机号码 */
    private String transferNumber;

    /** 转接方式 */
    private Integer transferType;

    /** 允许被打断 1:是 0:否 */
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
    /** 1:开始节点 2:普通节点 3:跳转节点 */
    private Integer nodeType;

    /** 节点坐标 */
    private String coordinate;
    /** 转接坐席组 */
    private Integer transferAgentGroupId;
    /** 提取关键词 */
    private Integer triggerMatch;
    /** 关键词预测 */
    private Integer matchId;
    /** 设置意向等级 */
    private String levelName;

    private List<Integer> intentionIds;
    private List<TemplateVerbalContentVo> verbalList;
    /** 0-顺序播报 1-随机播报 */
    private Integer verbalType;
    private Integer targetFlowId;
}
