package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 交互全局设置对象 efit_template_global_interaction
 * 
 * @author across
 * @date 2025-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateGlobalInteractionDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 启用打断设置 */
    private Integer enableInterrupt;

    /** 多少秒不能打断 */
    private Integer seconds;

    /** 交互设置启用 */
    private Integer enableInteraction;

    /** 最大交互轮次 */
    private Integer maxInteractiveCount;

    /** 最大通话时长，单位：分钟 */
    private Integer maxDuration;

    /** 触发动作 1-挂机 0-跳转节点 */
    private Integer interactionAction;

    /** 目标节点id */
    private Integer targetFlowId;

    /** 话术id */
    private Integer verbalId;

}
