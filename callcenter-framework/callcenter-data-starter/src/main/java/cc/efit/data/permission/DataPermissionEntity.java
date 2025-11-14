package cc.efit.data.permission;

import cc.efit.org.permission.OrgBaseEntity;
import cc.efit.web.utils.SecurityUtils;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@MappedSuperclass
public class DataPermissionEntity extends OrgBaseEntity {
    /** 用户id */
    @Immutable
    private Integer userId;

    /** 部门名称 */
    @Immutable
    private Integer deptId;

    @PrePersist
    public void dataPrePersist() {
        if (this.userId == null) {
            this.userId = SecurityUtils. getCurrentUserId();
        }
        if (this.deptId == null) {
            this.deptId = SecurityUtils. getCurrentDeptId();
        }
    }
}
