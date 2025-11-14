package cc.efit.data.permission;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "data")
public class DataPermissionProperties {

    private boolean enable = false;

    private String columnDeptId = "dept_id";

    private String columnUserId = "user_id";

    private Set<String> ignoreTable;
}