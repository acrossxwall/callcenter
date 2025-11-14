package cc.efit.org.permission;

import cc.efit.db.base.BaseEntity;
import cc.efit.web.utils.SecurityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@MappedSuperclass
public class OrgBaseEntity extends BaseEntity {

    @Column(name = "org_id")
    @Immutable
    private Integer orgId;

    @PrePersist
    public void orgPrePersist() {
        if (this.orgId == null) {
            this.orgId = SecurityUtils.getCurrentUserOrgId();
        }
    }
}
