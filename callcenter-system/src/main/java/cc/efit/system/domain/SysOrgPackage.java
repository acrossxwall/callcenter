package cc.efit.system.domain;

import cc.efit.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 租户套餐表对象 efit_sys_org_package
 * 
 * @author across
 * @date 2025-10-28
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_sys_org_package")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class SysOrgPackage extends BaseEntity implements Serializable {

    /** 套餐编号 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 套餐名 */
    private String name;
    /** 租户状态（1正常 0停用） */
    private Integer status;
    /** 备注 */
    private String remark;
}
