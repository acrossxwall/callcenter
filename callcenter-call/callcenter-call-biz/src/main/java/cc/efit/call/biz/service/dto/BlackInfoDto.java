package cc.efit.call.biz.service.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 黑名单库表对象 efit_call_black_info
 * 
 * @author across
 * @date 2025-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BlackInfoDto extends BaseDTO implements Serializable {

    /** 主键ID */
    private Integer id;

    /** 用户姓名 */
    private String name;

    /** 用户号码 */
    private String phone;

    /** 来源，1:页面导入，2:api导入 */
    private Integer source;

    /** 过期时间 */
    private LocalDate expireTime;

    /** 备注 */
    private String remark;

}
