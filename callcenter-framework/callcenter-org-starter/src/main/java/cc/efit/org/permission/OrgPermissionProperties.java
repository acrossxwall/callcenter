package cc.efit.org.permission;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "org")
public class OrgPermissionProperties {

    private boolean enable = false;

    private String column = "org_id";

    private Set<String> ignoreTable;
}
