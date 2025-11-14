package cc.efit.org.utils;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.statement.select.Select;

import java.util.Set;

public class ExpressionVisitorAdapterCustomer<T extends Expression> extends ExpressionVisitorAdapter<AndExpression> {
    private Set<String> ignoreTables ;
    private String column ;
    private Integer orgId ;
    public ExpressionVisitorAdapterCustomer(Set<String> ignoreTables,String column,Integer orgId) {
        this.ignoreTables = ignoreTables;
        this.column = column;
        this.orgId = orgId;
    }
    @Override
    public <S>  AndExpression visit(AndExpression andExpression,S context) {
        processAndExpression(andExpression,ignoreTables,column,orgId);
        return andExpression;
    }

    private void processAndExpression(Expression expression, Set<String> ignoreTables, String column, Integer orgId) {
        switch (expression) {
            case Select select -> {
                FormatSelectItemUtil.processSelect(select.getPlainSelect(), ignoreTables, column, orgId);
            }
            case BinaryExpression binaryExp -> {
                processAndExpression(binaryExp.getLeftExpression(), ignoreTables, column, orgId);
                processAndExpression(binaryExp.getRightExpression(), ignoreTables, column, orgId);
            }
            case NotExpression notExp -> processAndExpression(notExp.getExpression(), ignoreTables, column, orgId);
            case ExistsExpression existsExp ->
                    processAndExpression(existsExp.getRightExpression(), ignoreTables, column, orgId);
            case InExpression inExpression -> {
                processAndExpression(inExpression.getLeftExpression(), ignoreTables, column, orgId);
                processAndExpression(inExpression.getRightExpression(), ignoreTables, column, orgId);
            }
            case null, default -> {
            }
        }
    }
}