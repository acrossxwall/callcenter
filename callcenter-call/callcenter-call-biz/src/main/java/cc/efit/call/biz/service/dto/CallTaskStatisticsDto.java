package cc.efit.call.biz.service.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
/**
 * 呼叫任务统计表表对象 efit_call_task_statistics
 * 
 * @author across
 * @date 2025-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTaskStatisticsDto extends BaseDTO implements Serializable {

    /** ID */
    private Integer id;

    /** 任务id */
    private Integer taskId;

    /** 统计时间 */
    private LocalDateTime callDate;

    /** 导入数 */
    private Integer totalCustomers;

    /** 呼叫数 */
    private Integer calledCustomers;

    /** 接通数 */
    private Integer connectCount;

    /** 通话时长 */
    private Integer duration;

    /** 短信发送量 */
    private Integer smsCount;

    /** 短信成功量 */
    private Integer smsSuccessCount;

    /** 短信计费量 */
    private Integer smsBillCount;

}
