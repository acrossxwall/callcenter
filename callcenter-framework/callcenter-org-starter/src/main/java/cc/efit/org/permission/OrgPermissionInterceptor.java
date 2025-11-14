package cc.efit.org.permission;

import cc.efit.org.CustomerPermissionInspector;
import cc.efit.org.GlobalPermissionHolder;
import cc.efit.org.utils.FormatSelectItemUtil;
import cc.efit.web.utils.SecurityUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@Setter
@Slf4j
public class OrgPermissionInterceptor implements CustomerPermissionInspector {
    private OrgPermissionProperties permissionProperties;

    @Override
    public String inspect(String sql) {
//        log.info("org origin sql:{}",sql);
        if (!permissionProperties.isEnable()
                || GlobalPermissionHolder.isIgnore() || OrgPermissionHolder.isIgnore()
                || SecurityUtils.getCurrentUserIsSystemAdmin()){
            //说明未开启机构过滤 或者当前线程忽略
            return sql;
        }
        try {
            Integer orgId = SecurityUtils.getCurrentUserOrgId();
            if (orgId==null || orgId==0){
                //从当前线程获取
                orgId = OrgPermissionHolder.getCurrentOrgId();
            }
            sql = processDmlStatement(sql,permissionProperties.getIgnoreTable(),orgId,permissionProperties.getColumn()  );
        }catch (Exception e){
            //获取orgId 失败，可能是junit测试，不处理
            log.error("解析sql失败",e);
        }
//        log.info("org permission sql:{}",sql);
        return sql;
    }

    public String processDmlStatement(String sql, Set<String> ignoreTable, Integer orgId,String column) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select select) {
            FormatSelectItemUtil.processSelect(  select.getPlainSelect(),ignoreTable ,column,orgId);
            return select.toString();
        } else if (statement instanceof Update update) {
            Table table = update.getTable();
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : "";
            if (ignoreTable==null || !ignoreTable.contains(tableName.toLowerCase())) {
                update.setWhere(buildWhereOrgExpression(orgId, column, update.getWhere(), alias));
            }
            return update.toString();
        } else if (statement instanceof Delete delete) {
            Table table = delete.getTable();
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : "";
            if (ignoreTable==null || !ignoreTable.contains(tableName.toLowerCase())) {
                delete.setWhere(buildWhereOrgExpression(orgId, column, delete.getWhere(), alias));
            }
            return delete.toString();
        }else if (statement instanceof Insert insert) {
            Table table = insert.getTable();
            String tableName = table.getName();
            ExpressionList<Column> list = insert.getColumns();
            boolean containsColumn = FormatSelectItemUtil.containsColumn(column, list);
            if (!containsColumn && ( ignoreTable==null || !ignoreTable.contains(tableName.toLowerCase()))) {
                list.add(new Column(column));
                insert.getValues().addExpressions(new LongValue(orgId));
                return insert.toString();
            }
        }
        return sql;
    }

    private Expression buildWhereOrgExpression(Integer orgId, String column, Expression where, String alias) {
        Expression orgExp = new EqualsTo(
                new Column(StringUtils.isNotBlank(alias)? alias + "." + column : column),
                new LongValue(orgId)
        );
        return where==null?orgExp:new AndExpression(where, orgExp);
    }

}
