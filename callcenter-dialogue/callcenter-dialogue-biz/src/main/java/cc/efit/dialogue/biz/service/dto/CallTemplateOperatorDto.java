package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 话术模板操作日志对象 efit_call_template_operator
 * 
 * @author across
 * @date 2025-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTemplateOperatorDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 部门id */
    private Integer deptId;

    /** 用户id */
    private Integer userId;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 操作类型 1-增加 2-删除 3-修改 */
    private Integer type;

    /** 操作内容 */
    private String content;

    /** 操作ip */
    private String ip;

    private Integer orgId;

}
