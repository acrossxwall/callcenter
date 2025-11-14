package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 无应答全局设置对象 efit_template_global_noreply
 * 
 * @author across
 * @date 2025-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateGlobalNoreplyDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 无应答设置启用 */
    private Integer enableNoreply;

    /** 最大无应答轮次 */
    private Integer maxNoreplyCount;

    /** 最大无应答时长，单位：秒 */
    private Integer maxNoreplySeconds;

    /** 触发动作 1-挂机 0-跳转节点 */
    private Integer noreplyAction;

    /** 目标节点id */
    private Integer targetFlowId;

    /** 话术id */
    private Integer verbalId;

}
