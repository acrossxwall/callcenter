package cc.efit.call.biz.service.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
/**
 * 呼叫任务系统维度统计表对象 efit_call_system_statistics
 * 
 * @author across
 * @date 2025-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallSystemStatisticsDto extends BaseDTO implements Serializable {

    /** ID */
    private Integer id;

    /** 统计日期 */
    private LocalDate callDate;

    /** 统计时间 */
    private String callTime;

    /** 导入客户数 */
    private Integer totalCustomers;

    /** 已呼叫客户数 */
    private Integer calledCustomers;

    /** 接通用户数 */
    private Integer connectCount;

    /** 通话时长 */
    private Integer duration;

    /** 并发数 */
    private Integer concurrent;

}
