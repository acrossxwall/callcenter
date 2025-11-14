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
 * 知识库对象 efit_template_knowledge
 * 
 * @author across
 * @date 2025-08-16
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_knowledge")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateKnowledge extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 名称 */
    private String name;
    /** 1:业务问题,2:一般问题 */
    private Integer type;
    /** 意图id */
    private Integer intentionId;
    /** 话术ids */
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Integer> verbalIds;
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
}
