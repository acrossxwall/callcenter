package cc.efit.org;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class CompositeStatementInspector implements StatementInspector {

    private final List<CustomerPermissionInspector> inspectors = new ArrayList<>();

    public void addInspector(CustomerPermissionInspector inspector) {
        inspectors.add(inspector);
    }

    @Override
    public String inspect(String sql) {
        log.info("origin sql:{}",sql);
        log.info("inspectors size :{}",inspectors.size());
        String processedSql = sql;
        // 遍历所有拦截器，将上一个拦截器处理后的 SQL 传给下一个
        for (CustomerPermissionInspector inspector : inspectors) {
            processedSql = inspector.inspect(processedSql);
        }
        log.info("inspector sql:{}",sql);
        return processedSql;
    }
}