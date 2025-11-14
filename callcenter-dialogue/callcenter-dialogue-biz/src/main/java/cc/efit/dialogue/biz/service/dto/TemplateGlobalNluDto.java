package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
/**
 * nlu全局设置对象 efit_template_global_nlu
 * 
 * @author across
 * @date 2025-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateGlobalNluDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 模板id */
    private Integer callTemplateId;

    /** nlu设置启用 */
    private Integer enableNlu;

    /** 模板id */
    private String modeId;

    /** 匹配阈值 */
    private double threshold;

}
