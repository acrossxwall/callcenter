package cc.efit.dialogue.api.vo.global;

public record TemplateGlobalNluInfo(
        boolean enabled,
        String modelName,
        double threshold
) {
}
