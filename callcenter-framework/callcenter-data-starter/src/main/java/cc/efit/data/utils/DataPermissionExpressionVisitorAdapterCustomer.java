package cc.efit.data.utils;

import cc.efit.web.base.UserDataScope;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.statement.select.Select;

import java.util.Set;

public class DataPermissionExpressionVisitorAdapterCustomer<T extends Expression> extends ExpressionVisitorAdapter<AndExpression> {
    private Set<String> ignoreTables ;
    private String deptColumn;
    private String userColumn;
    private UserDataScope dataScope;
    private Integer userId ;
    public DataPermissionExpressionVisitorAdapterCustomer(Set<String> ignoreTables, String deptColumn, String userColumn,
                                                          UserDataScope dataScope, Integer userId) {
        this.ignoreTables = ignoreTables;
        this.deptColumn = deptColumn;
        this.userColumn = userColumn;
        this.dataScope = dataScope;
        this.userId = userId;
    }
    @Override
    public <S>  AndExpression visit(AndExpression andExpression,S context) {
        processAndExpression(andExpression,ignoreTables,deptColumn,userColumn,dataScope,userId);
        return andExpression;
    }

    private void processAndExpression(Expression expression, Set<String> ignoreTables, String deptColumn, String userColumn,
                                      UserDataScope dataScope, Integer userId) {
        switch (expression) {
            case Select select -> {
                FormatDataPermissionUtil.processSelect(select.getPlainSelect(), ignoreTables, deptColumn, userColumn, dataScope, userId);
            }
            case BinaryExpression binaryExp -> {
                processAndExpression(binaryExp.getLeftExpression(), ignoreTables, deptColumn, userColumn, dataScope, userId);
                processAndExpression(binaryExp.getRightExpression(), ignoreTables, deptColumn, userColumn, dataScope, userId);
            }
            case NotExpression notExp ->
                    processAndExpression(notExp.getExpression(), ignoreTables, deptColumn, userColumn, dataScope, userId);
            case ExistsExpression existsExp ->
                    processAndExpression(existsExp.getRightExpression(), ignoreTables, deptColumn, userColumn, dataScope, userId);
            case InExpression inExpression -> {
                processAndExpression(inExpression.getLeftExpression(), ignoreTables, deptColumn, userColumn, dataScope, userId);
                processAndExpression(inExpression.getRightExpression(), ignoreTables, deptColumn, userColumn, dataScope, userId);
            }
            case null, default -> {
            }
        }
    }
}