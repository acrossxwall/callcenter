package cc.efit.call.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 线路分配表对象 efit_call_line_assign
 * 
 * @author across
 * @date 2025-08-28
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_line_assign")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class LineAssign extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 线路id */
    private Integer lineId;
    /** 分配线路部门id */
    private Integer assignDeptId;
    /** 分配并发 */
    private Integer concurrency;
}
