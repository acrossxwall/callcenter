package cc.efit.dialogue.biz.vo.global;

public record TemplateGlobalTtsInfo(boolean enabled, String engine, String voiceType,
                                    String speed, String volume, String pitch) {
}
