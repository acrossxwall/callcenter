package cc.efit.dialogue.biz.domain;

import cc.efit.dialogue.biz.vo.node.EdgeInfo;
import lombok.*;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
/**
 * 节点意图分支对象 efit_template_flow_branch
 * 
 * @author across
 * @date 2025-08-15
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_flow_branch")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
@Builder
public class TemplateFlowBranch extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 话术模板id */
    private Integer callTemplateId;
    /** 节点id */
    private Integer flowId;
    /** 意图id */
    private Integer intentionId;
    /** 目标flowid */
    private Integer targetFlowId;
    /** 连线信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    private EdgeInfo edgeInfo;

    private Integer sort;

}
