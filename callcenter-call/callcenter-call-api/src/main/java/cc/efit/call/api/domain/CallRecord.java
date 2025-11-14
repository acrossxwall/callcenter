package cc.efit.call.api.domain;

import java.time.LocalDateTime;

import cc.efit.process.api.core.InteractiveRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 呼叫记录表对象 efit_call_record
 * 
 * @author across
 * @date 2025-09-26
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_record")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallRecord extends DataPermissionEntity implements Serializable {

    /** 记录ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 任务ID */
    private Integer taskId;
    /** 任务名称 */
    private String taskName;
    /** 模板id */
    private Integer callTemplateId;
    /** 模板名称 */
    private String callTemplateName;
    /** 客户ID */
    private Integer customerId;
    /** 呼叫id */
    private String callId;
    /** 线路id */
    private Integer lineId;
    /** 线路名称 */
    private String lineName;
    /** 主叫 */
    private String callNumber;
    /** 外呼号码 */
    private String phone;
    /** 客户信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String,String> customerInfo;
    /** 呼叫时间 */
    private LocalDateTime callTime;
    /** 接通时间 */
    private LocalDateTime answerTime;
    /** 挂断时间 */
    private LocalDateTime hangupTime;
    /** 振铃时长(秒) */
    private Integer ringSeconds;
    /** 挂断原因 */
    private String hangupReason;
    /** 意向等级 */
    private String intentionLevel;
    /** 意向描述 */
    private String intentionName;
    /** 通话时长(秒) */
    private Integer duration;
    /** 呼叫状态 0-未接听 1-已接听 */
    private Integer status;
    /** 录音地址 */
    private String recordPath;
    /** 交互记录 */
    @JdbcTypeCode(SqlTypes.JSON)
    private List<InteractiveRecord> interactiveRecord;
    /** 交互轮次 */
    private Integer interactiveCount;
    /** 自定义标签 */
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;
}
