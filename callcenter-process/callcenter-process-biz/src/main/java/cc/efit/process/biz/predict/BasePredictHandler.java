package cc.efit.process.biz.predict;

import cc.efit.process.api.res.MatchResult;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.predict.keyword.Keyword;
import cc.efit.process.biz.predict.utils.JexlExpressionUtils;
import cc.efit.utils.RegexUtils;

public interface BasePredictHandler {

    MatchResult predictFlow(String content, TemplateNodeInfo flowNode, DialogueProcessSession session) ;

    String getHandlerType();

    default boolean  matchLogic(Keyword keyword, String content) {
        return switch (keyword.type()) {
            //1正则
            case 1 -> RegexUtils.matchContent(content,keyword.value());
            //2逻辑表达式
            case 2 -> JexlExpressionUtils.evaluateExpression( content, keyword.value());
            default -> false;
        };
    }
}
