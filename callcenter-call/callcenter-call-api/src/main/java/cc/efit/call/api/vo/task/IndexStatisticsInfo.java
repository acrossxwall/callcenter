package cc.efit.call.api.vo.task;

import java.util.List;

public record IndexStatisticsInfo (
        /**
         * 时间
         */
        List<String> timeList,
        /**
         * 呼叫量
         */
        List<Integer> callList,
        /**
         * 接通量
         */
        List<Integer> answerList,
        /**
         * 接通率 保留整数
         */
        List<Integer> rateList,
        /**
         * 并发数
         */
        List<Integer> concurrentList
) {
}
