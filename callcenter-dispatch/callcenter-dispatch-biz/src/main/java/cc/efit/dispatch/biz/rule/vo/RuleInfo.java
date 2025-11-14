package cc.efit.dispatch.biz.rule.vo;

public record RuleInfo(
        Integer condition,
        Integer operation,
        String value
) {
}
