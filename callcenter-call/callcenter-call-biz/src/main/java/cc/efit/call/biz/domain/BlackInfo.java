package cc.efit.call.biz.domain;

import java.time.LocalDate ;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 黑名单库表对象 efit_call_black_info
 * 
 * @author across
 * @date 2025-08-27
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_black_info")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class BlackInfo extends DataPermissionEntity implements Serializable {

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 用户姓名 */
    private String name;
    /** 用户号码 */
    private String phone;
    /** 来源，1:页面导入，2:api导入 */
    private Integer source;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireTime;
    /** 备注 */
    private String remark;
}
