package cc.efit.process.biz.predict;

import cc.efit.dialogue.api.enums.TemplateIntentionClassifyEnum;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.base.CommonHandler;

public abstract class AbstractPredictHandler implements BasePredictHandler, CommonHandler {

    public String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        text = text.toLowerCase();
        text = text.trim();
        text = text.replaceAll("\\s+", " ");
        text = text.replaceAll("[\\p{Punct}\\p{S}]+", "");
        return text;
    }

    public void buildSessionIntentionClassify(DialogueProcessSession session, Integer classify) {
        if (TemplateIntentionClassifyEnum.POSITIVE.getCode().equals(classify)) {
            //肯定意图
            session.setAffirmCount(session.getAffirmCount() + 1);
        }else if (TemplateIntentionClassifyEnum.NEGATIVE.getCode().equals(classify)) {
            //否定意图
            session.setNegativeCount(session.getNegativeCount() + 1);
        }else if (TemplateIntentionClassifyEnum.REFUSE.getCode().equals(classify)) {
            session.setRefuseCount(session.getRefuseCount() + 1);
        }
    }
}
