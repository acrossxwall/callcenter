package cc.efit.data.utils;

import cc.efit.web.base.UserDataScope;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

public class FormatDataPermissionUtil {

    public static void processFromItem(FromItem fromItem, PlainSelect select, Set<String> ignoreTables, String deptColumn, String userColumn,
                                       UserDataScope dataScope, Integer userId) {
        if (fromItem instanceof Table table) {
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : "";
            if (ignoreTables==null || !ignoreTables.contains(tableName.toLowerCase())) {
                Expression dataExp = buildDataScopeExpression(userId,select.getWhere(),alias,deptColumn,userColumn,dataScope);
                select.setWhere(dataExp);
            }
        } else if (fromItem instanceof LateralSubSelect lateralSubSelect) {
            PlainSelect  plainSelect = lateralSubSelect.  getPlainSelect();
            processSelect(plainSelect,ignoreTables,deptColumn,userColumn,dataScope,userId);
        } else if (fromItem instanceof ParenthesedSelect parenthesedSelect) {
            PlainSelect  plainSelect = parenthesedSelect.getPlainSelect() ;
            processSelect(plainSelect,ignoreTables,deptColumn,userColumn,dataScope,userId);
        }
    }

    public static void processSelect(PlainSelect select, Set<String> ignoreTables, String deptColumn, String userColumn,
                                     UserDataScope dataScope, Integer userId) {
        processFromItem(select.getFromItem(), select,ignoreTables,deptColumn,userColumn,dataScope,userId);
        if (select.getJoins() != null) {
            for (Join join : select.getJoins()) {
                processFromItem(join.getRightItem(), select,ignoreTables,deptColumn,userColumn,dataScope,userId);
            }
        }
        // 递归处理 where 里的子查询
        processSubSelect(select.getWhere(),ignoreTables,deptColumn,userColumn,dataScope,userId);
    }

    private static  void processSubSelect(Expression where,Set<String> ignoreTables,String deptColumn, String userColumn,
                                          UserDataScope dataScope,Integer userId) {
        if (where != null) {
            where.accept(new DataPermissionExpressionVisitorAdapterCustomer<AndExpression>(ignoreTables,deptColumn,userColumn,dataScope,userId));
        }
    }

    public static Expression buildDataScopeExpression(Integer userId,
                                                Expression where, String alias,
                                                String deptColumn, String userColumn,
                                                UserDataScope dataScope) {

        Set<Integer> deptIds = dataScope.deptIds();
        boolean self = dataScope.self() && CollectionUtils.isEmpty(deptIds);
        Expression expression;
        if (self) {
            expression = new EqualsTo(
                    new Column(StringUtils.isNotBlank(alias)? alias + "." + userColumn : userColumn),
                    new LongValue(userId)
            );
        }else{
            Integer deptId = deptIds.stream().findFirst().orElse(null);
            if (deptIds.size()==1) {
                expression =   new EqualsTo(
                        new Column(StringUtils.isNotBlank(alias)? alias + "." + deptColumn : deptColumn),
                        new LongValue(deptId)
                );
            }else{
                List<LongValue> expressions = dataScope.deptIds().stream()
                        .map(LongValue::new)
                        .toList();
                expression = new InExpression(
                        new Column(StringUtils.isNotBlank(alias)? alias + "." + deptColumn : deptColumn),
                        new ParenthesedExpressionList<>(expressions)
                );
            }
        }
        return where == null ? expression : new AndExpression(where, expression);
    }
}
