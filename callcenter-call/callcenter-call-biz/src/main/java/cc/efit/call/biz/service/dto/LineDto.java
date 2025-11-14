package cc.efit.call.biz.service.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
/**
 * 中继线路网关表对象 efit_call_line
 * 
 * @author across
 * @date 2025-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 域名或ip */
    private String realm;

    /** 端口 */
    private Integer port;

    /** 认证的用户名 */
    private String username;

    /** 认证的密码 */
    private String password;

    /** 是否注册，默认0:非注册，1:注册 */
    private Integer register;

    /** 网关名 */
    private String gatewayName;

    /** 注册状态：0:初始，1:成功，2:失败 */
    private Integer regStatus;

    /** 线路名称 */
    private String lineName;

    /** 主叫号码 */
    private String callNumber;

    /** 并发总数 */
    private Integer concurrency;

    /** 单价（元/分钟） */
    private BigDecimal unitPrice;

    /** 状态：1:开，0:关 */
    private Integer status;

    /** 呼出前缀 */
    private String callPrefix;

    /** 备注 */
    private String remark;

}
