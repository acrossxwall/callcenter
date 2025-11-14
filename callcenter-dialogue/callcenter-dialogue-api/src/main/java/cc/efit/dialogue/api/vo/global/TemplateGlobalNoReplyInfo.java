package cc.efit.dialogue.api.vo.global;

public record TemplateGlobalNoReplyInfo(boolean enabled, Integer maxNoreplyCount,
                                        Integer maxNoreplySeconds, Integer noreplyAction, Integer targetFlowId,
                                        String verbalFilePath, String verbalFileText, Integer fileId ,String fileName) {
}
