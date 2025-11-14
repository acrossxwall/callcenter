package cc.efit.dial.api.core;

import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.api.req.LineInfoReq;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DialProcessSession {
    /**
     * 任务id
     */
    private Integer taskId;
    /**
     * 呼叫id
     */
    private String callId;
    /**
     * 呼叫uuid 播音 打断 挂断使用
     */
    private String callUuid;
    /**
     * 线路信息
     */
    private LineInfoReq lineInfo;
    /**
     * 客户信息
     */
    private CallCustomerInfoReq customer;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 呼叫时间
     */
    private LocalDateTime callTime;
    /**
     * 接听时间
     */
    private LocalDateTime answerTime;
    /**
     * 挂断时间
     */
    private LocalDateTime hangupTime;
    /**
     * 播音开始时间
     */
    private LocalDateTime playStartTime;
    /**
     * 播音结束时间
     */
    private LocalDateTime playStopTime;
    /**
     * 会话状态
     */
    private ProcessSessionStatusEnum status;
    /**
     * 挂断原因
     */
    private String hangupReason;
    /**
     * 挂断类型
     */
    private Integer hangupType;
    /**
     * 打断类型
     */
    private boolean enableInterrupt;
    /**
     * 前多少s不能打断
     */
    private Integer interruptSeconds;
    /**
     * 启用无应答时长
     */
    private boolean enableNoReply;
    /**
     * 无应答超时时间
     */
    private Integer noReplySeconds;
    /**
     * asr结果
     */
    private List<String> asrResult;
    /**
     * 是否释放信号量
     */
    private boolean releaseSemaphore = true;
    /**
     * 是否风险处理
     */
    private boolean riskHandler = false;
    /**
     * 封控数据处理
     */
    private Integer callStatus;
}
