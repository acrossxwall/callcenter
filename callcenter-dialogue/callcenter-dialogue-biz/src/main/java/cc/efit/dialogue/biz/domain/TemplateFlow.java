package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

import java.util.List;
/**
 * 流程节点对象 efit_template_flow
 * 
 * @author across
 * @date 2025-08-14
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_flow")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateFlow extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 节点名称 */
    private String nodeName;
    /** 话术模板ID */
    private Integer callTemplateId;
    /** 是否挂断  */
    private Integer enableHangup;
    /** 挂机方式 0直接挂机: 1:回复话术后挂机 */
    private Integer hangupMode;
    /** 转人工 0-否 1-是 */
    private Integer enableTransfer;
    /** 转接坐席的号码 */
    private String transferNumber;
    /** 电话号码 1 坐席组2 */
    private Integer transferType;
    /** 允许打断  */
    private Integer enableInterrupt;
    /** 匹配知识库  */
    private Integer enableMatchKnowledge;
    /** 匹配知识库 */
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Integer> matchKnowledgeIds;
    /** 忽略用户回复 */
    private Integer enableIgnoreReply;
    /** 用户回复0:忽略用户回复 1:忽略用户除拒绝外的所有回复 2:都不忽略 */
    private Integer ignoreReplyType;

    /**  发送短信 0-否 1-是 */
    private Integer enableSendSms;
    /** 短信模板 */
    private Integer smsTemplateId;
    /** 标签 */
    private String nodeLabel;
    /** 1:开始节点 2:普通节点 3:跳转节点 */
    private Integer nodeType;
    /** 话术ids */
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Integer> verbalIds;
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
    /** 0-顺序播报 1-随机播报 */
    private Integer verbalType;
    private Integer targetFlowId;
}
