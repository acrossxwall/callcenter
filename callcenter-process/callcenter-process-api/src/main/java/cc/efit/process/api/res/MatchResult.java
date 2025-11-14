package cc.efit.process.api.res;

public record MatchResult(
        Integer flowId,
        String keyword,
        Integer intentionId,
        String intentionName,
        boolean keywordMatch,
        boolean nluMatch,
        String nluMatchCode
) {
}
