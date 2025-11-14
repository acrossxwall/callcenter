package cc.efit.nlu.res;

public record PredictCandidatesItem(
        /**
         * 意图
         */
        String intent,
        /**
         * 分数
         */
        double score
) {
}
