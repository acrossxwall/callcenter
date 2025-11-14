package cc.efit.org.utils;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class FormatSelectItemUtil {

    public static void processFromItem(FromItem fromItem, PlainSelect select, Set<String> ignoreTables, String column, Integer orgId) {
        if (fromItem instanceof Table table) {
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : "";
            if (ignoreTables==null || !ignoreTables.contains(tableName.toLowerCase())) {
                Expression orgExp = new EqualsTo(
                        new Column(StringUtils.isNotBlank(alias)?alias + "." + column:column),
                        new LongValue(orgId)
                );
                if (select.getWhere() == null) {
                    select.setWhere(orgExp);
                } else {
                    select.setWhere(new AndExpression(select.getWhere(), orgExp));
                }
            }
        } else if (fromItem instanceof LateralSubSelect lateralSubSelect) {
            PlainSelect  plainSelect = lateralSubSelect.  getPlainSelect();
            processSelect(plainSelect,ignoreTables,column,orgId);
        } else if (fromItem instanceof ParenthesedSelect parenthesedSelect) {
            PlainSelect  plainSelect = parenthesedSelect.getPlainSelect() ;
            processSelect(plainSelect,ignoreTables,column,orgId);
        }
    }

    public static void processSelect(PlainSelect select,Set<String> ignoreTables,String column,Integer orgId) {
        processFromItem(select.getFromItem(), select,ignoreTables,column,orgId);
        if (select.getJoins() != null) {
            for (Join join : select.getJoins()) {
                processFromItem(join.getRightItem(), select,ignoreTables,column,orgId);
            }
        }
        // 递归处理 where 里的子查询
        processSubSelect(select.getWhere(),ignoreTables,column,orgId);
    }

    private static  void processSubSelect(Expression where,Set<String> ignoreTables,String column,Integer orgId) {
        if (where != null) {
            where.accept(new ExpressionVisitorAdapterCustomer<AndExpression>(ignoreTables,column,orgId));
        }
    }

    public static boolean containsColumn(String column, ExpressionList<Column> list) {
        boolean contains = false;
        for (Column item : list) {
            if (item.getColumnName().equalsIgnoreCase(column)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}
