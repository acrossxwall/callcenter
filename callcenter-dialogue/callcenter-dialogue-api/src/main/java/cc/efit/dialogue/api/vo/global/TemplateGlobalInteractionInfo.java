package cc.efit.dialogue.api.vo.global;

public record TemplateGlobalInteractionInfo(boolean enabled, Integer maxInteractiveCount,
                                            Integer maxDuration, Integer interactionAction, Integer targetFlowId,
                                            String verbalFilePath, String verbalFileText,Integer fileId,
                                            String fileName) {
}
