package cc.efit.process.biz.predict.utils;

import cc.efit.process.biz.cache.IntentionCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;

import java.util.function.BiFunction;
import java.util.regex.Pattern;
@Slf4j
public class JexlExpressionUtils {
    private JexlExpressionUtils(){}
    private static final JexlEngine jexl = new JexlBuilder().create();

    public static boolean evaluateExpression(String expr, String content) {
        try {
            JexlExpression jexlExpr = IntentionCache.expressionCache.get(expr, jexl::createExpression);
            // 绑定变量
            JexlContext context = new MapContext();
            context.set("content", content);
            context.set("text", content);
            context.set("matches", (BiFunction<String, String, Boolean>) Pattern::matches);
            context.set("contains", (BiFunction<String, String, Boolean>) String::contains);

            assert jexlExpr != null;
            Object result = jexlExpr.evaluate(context);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("evaluateExpression error", e);
            return false;
        }
    }
}
