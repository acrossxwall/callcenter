package cc.efit.dialogue.biz.vo.global;

public record TemplateGlobalAdvanceInfo(boolean enabled, boolean silenceDetection,
                                        int silenceThreshold,boolean emotionRecognition,
                                        boolean noiseReduction) {
}
