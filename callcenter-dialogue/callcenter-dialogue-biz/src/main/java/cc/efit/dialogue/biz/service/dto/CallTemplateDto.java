package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
/**
 * ai拨打话术对象 efit_call_template
 * 
 * @author across
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTemplateDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 部门名称 */
    private Integer deptId;

    /** 用户id */
    private Integer userId;

    /** 话术名称 */
    private String name;

    /** 话术描述 */
    private String description;

    /** 行业 */
    private String industry;

    /** 状态 */
    private Integer status;

    /** 机构id */
    private Integer orgId;

}
