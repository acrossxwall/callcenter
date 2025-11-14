package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 话术模板操作日志对象 efit_call_template_operator
 * 
 * @author across
 * @date 2025-08-12
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_template_operator")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallTemplateOperator extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 话术模板id */
    private Integer callTemplateId;
    /** 操作类型 1-增加 2-删除 3-修改 */
    private Integer type;
    /** 操作内容 */
    private String content;
    /** 操作ip */
    private String ip;
}
