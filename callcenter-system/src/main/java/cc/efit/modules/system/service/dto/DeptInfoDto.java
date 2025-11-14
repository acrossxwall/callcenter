package cc.efit.modules.system.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author across
 * @Description
 * @Date 2025-08-02 10:49
 */
@Data
public class DeptInfoDto {

    private Integer id;
    private String name;

    private List<DeptInfoDto> children;
}
