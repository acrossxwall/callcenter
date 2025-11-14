package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 线路分配表对象 efit_call_line_assign
 * 
 * @author across
 * @date 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineAssignDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 线路id */
    private Integer lineId;

    /** 分配线路部门id */
    private Integer assignDeptId;

    /** 分配并发 */
    private Integer concurrency;

}
