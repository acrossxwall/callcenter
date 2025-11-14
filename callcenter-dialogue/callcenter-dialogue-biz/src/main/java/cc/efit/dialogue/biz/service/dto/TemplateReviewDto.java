package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 话术审核记录对象 efit_template_review
 * 
 * @author across
 * @date 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateReviewDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 审核备注 */
    private String remark;

    /** 话术模板名称 */
    private String name;

    /** 2-审核中 3-审核通过 4-审核失败 */
    private Integer status;

    private Timestamp checkTime;
}
