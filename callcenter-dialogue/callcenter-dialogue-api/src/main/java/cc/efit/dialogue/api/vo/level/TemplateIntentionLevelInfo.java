package cc.efit.dialogue.api.vo.level;

public record TemplateIntentionLevelInfo(
        /** 意向等级 */
         String name,
        /** 描述 */
         String description,
        /** 类型 1-默认 0-非默认 */
         Integer type,
        /** 规则内容 */
         String ruleContent
) {
}
