package cc.efit.data.permission;

import cc.efit.data.utils.FormatDataPermissionUtil;
import cc.efit.org.CustomerPermissionInspector;
import cc.efit.org.GlobalPermissionHolder;
import cc.efit.web.base.UserDataScope;
import cc.efit.web.utils.SecurityUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.Set;

@Slf4j
@Setter
public class DataPermissionInterceptor implements CustomerPermissionInspector {
    private DataPermissionProperties permissionProperties;
    @Override
    public String inspect(String sql) {
//        log.info("data permission inspect sql:{}", sql);
        if (!permissionProperties.isEnable()
                || GlobalPermissionHolder.isIgnore() || DataPermissionHolder.isIgnore()
                || SecurityUtils.getCurrentUserIsOrgAdmin()){
            //说明未开启数据权限过滤 或者当前线程忽略 或者是租户管理员
            return sql;
        }
        try {
            UserDataScope dataScopeType = SecurityUtils.getCurrentUserDataScope();
            if (dataScopeType.all()) {
                log.info("当前用户是全部数据权限 do nothing");
                return sql;
            }
            Integer userId = SecurityUtils.getCurrentUserId();
            sql = processSql(sql, userId, dataScopeType);
        }catch (Exception e){
            log.error("data permission inspect error", e);
        }
//        log.info("data permission inspect result sql:{}", sql);
        return sql;
    }

    public String processSql(String sql,Integer userId, UserDataScope dataScopeType) throws JSQLParserException {
        Set<String> ignoreTable  = permissionProperties.getIgnoreTable();
        Statement statement = CCJSqlParserUtil.parse(sql);
        String userColumn = permissionProperties.getColumnUserId();
        String deptColumn = permissionProperties.getColumnDeptId();
        //insert 不处理
        //仅处理update select delete 语句
        if (statement instanceof Update update) {
            Table table = update.getTable();
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : "";
            if (ignoreTable==null || !ignoreTable.contains(tableName.toLowerCase())) {
                update.setWhere(FormatDataPermissionUtil.buildDataScopeExpression(userId, update.getWhere(), alias, deptColumn, userColumn,dataScopeType));
            }
            return update.toString();
        }else if (statement instanceof Delete delete) {
            Table table = delete.getTable();
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : "";
            if (ignoreTable==null || !ignoreTable.contains(tableName.toLowerCase())) {
                delete.setWhere(FormatDataPermissionUtil.buildDataScopeExpression(userId, delete.getWhere(), alias, deptColumn, userColumn,dataScopeType));
            }
            return delete.toString();
        }else if (statement instanceof Select select) {
            FormatDataPermissionUtil.processSelect(select.getPlainSelect(),ignoreTable , deptColumn, userColumn,dataScopeType,userId);
            return select.toString();
        }
        return sql;
    }

}
