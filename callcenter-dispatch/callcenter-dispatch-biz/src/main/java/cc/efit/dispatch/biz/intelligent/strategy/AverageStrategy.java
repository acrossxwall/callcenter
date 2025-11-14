package cc.efit.dispatch.biz.intelligent.strategy;

import cc.efit.call.api.vo.task.CallTaskConcurrency;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 平均策略
 */
@Slf4j
public class AverageStrategy extends AbstractStrategy {

    @Override
    protected int allocateCallTaskConcurrent(List<CallTaskConcurrency> taskList, int concurrency) {
        int size = taskList.size();
        int concurrent = concurrency / size;
        taskList.forEach(s->s.setConcurrency(concurrent));
        int allocated = taskList.stream().mapToInt(CallTaskConcurrency::getConcurrency).sum();
        int remaining  = concurrency - allocated;
        log.info("平均分总并发数:{}, 已分配并发数:{}, 剩余并发数:{}", concurrency, allocated, remaining);
        return remaining;
    }
}
