package cc.efit.dialogue.biz.vo.global;

import cc.efit.dialogue.api.vo.global.*;

public record TemplateGlobalSettingInfo(Integer callTemplateId, TemplateGlobalInterruptInfo interrupt,
                                        TemplateGlobalInteractionInfo interaction,
                                        TemplateGlobalNoReplyInfo noReply,
                                        TemplateGlobalTtsInfo tts,
                                        TemplateGlobalAdvanceInfo advanced,
                                        TemplateGlobalNluInfo nlu,
                                        TemplateGlobalDefaultVerbalInfo defaultVerbal) {
}
