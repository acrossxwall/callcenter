package cc.efit.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@Data
public class MenuTreeDto {

    /** 节点ID */
    private Integer id;

    /** 节点名称 */
    private String title;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuTreeDto> children;
}
