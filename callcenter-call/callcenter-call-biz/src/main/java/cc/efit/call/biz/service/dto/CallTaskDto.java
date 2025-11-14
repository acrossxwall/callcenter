package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 呼叫任务表对象 efit_call_task
 * 
 * @author across
 * @date 2025-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTaskDto extends BaseDTO implements Serializable {

    private Integer id;
    /** 任务名称 */
    private String taskName;
    /** 关联话术模板ID */
    private Integer callTemplateId;
    /** 话术模板名称 */
    private String templateName;
    /** 任务显示状态：0-待启动 1-运行中 */
    private Integer status;
    /** 任务调度状态 0-未启动，1-运行中，2-暂停中 3-已暂停 4-已完成 */
    private Integer callStatus;
    /** 总导入数 */
    private Integer totalCustomers;
    /** 已呼叫数 */
    private Integer calledCustomers;
    /** 总接通数 */
    private Integer connectCount;
    /** 今天导入数 */
    private Integer todayCustomers;
    /** 今天已呼叫数量 */
    private Integer todayCalled;
    /** 今天已接听数量 */
    private Integer todayConnect;
    /** 允许外呼时间 */
    private String allowTime;
    /** 禁止外呼时间 */
    private String denyTime;
    /** 计划拨打日期 */
    private String allowDay;
    /** 启用发送挂机短信 */
    private Integer enableSendSms;
    /** 挂机短信模板ID */
    private Integer smsTemplateId;
    /** 选择的线路id */
    private Integer lineId;
    /** 拨打并发数 */
    private Integer lineConcurrent;
    /** 1-重呼开启 0-关闭 */
    private Integer retryOpen;
    /** 重呼间隔时间 */
    private Integer retryInterval;
    /** 重呼条件 */
    private String retryCondition;
    /** 重呼次数 */
    private Integer retryCount;
    /** 备注 */
    private String remark;

}
