package cc.efit.dispatch.biz.intelligent.strategy;

import cc.efit.call.api.vo.task.CallTaskConcurrency;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 数据库设置并发按比例分配
 */
@Slf4j
public class SettingStrategy extends AbstractStrategy {
    @Override
    protected int allocateCallTaskConcurrent(List<CallTaskConcurrency> taskList, int concurrency) {
        int sumConcurrent = taskList.stream().mapToInt(CallTaskConcurrency::getLineConcurrent).sum();
        for (CallTaskConcurrency task : taskList) {
            double ratio = ((double) task.getLineConcurrent()) / sumConcurrent;
            double assigned = ratio * concurrency;
            int floor = (int) Math.floor(assigned);
            int allocation = Math.min(floor, task.getLineConcurrent() );
            task.setAssign(allocation);
            task.setConcurrency(allocation);
        }
        int allocated = taskList.stream().mapToInt(CallTaskConcurrency::getConcurrency).sum();
        int remaining = concurrency - allocated;
        log.info("数据库设置比例 总并发数:{}, 已分配并发数:{}, 剩余并发数:{}", concurrency, allocated, remaining );
        return remaining;
    }
}
