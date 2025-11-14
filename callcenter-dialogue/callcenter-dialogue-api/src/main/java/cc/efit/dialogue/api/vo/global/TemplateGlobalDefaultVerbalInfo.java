package cc.efit.dialogue.api.vo.global;

public record TemplateGlobalDefaultVerbalInfo(boolean enabled, Integer defaultAction, Integer targetFlowId,
                                              String verbalFilePath, String verbalFileText, Integer fileId , String fileName) {
}
