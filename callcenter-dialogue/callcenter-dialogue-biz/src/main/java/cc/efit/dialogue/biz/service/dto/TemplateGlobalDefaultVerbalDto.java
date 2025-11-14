package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
/**
 * 兜底话术全局设置对象 efit_template_global_default_verbal
 * 
 * @author across
 * @date 2025-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateGlobalDefaultVerbalDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 模板id */
    private Integer callTemplateId;

    /** 兜底话术设置启用 */
    private Integer enableDefault;

    /** 触发动作 1-挂机 0-跳转节点 */
    private Integer defaultAction;

    /** 目标节点id */
    private Integer targetFlowId;

    /** 话术id */
    private Integer verbalId;

}
