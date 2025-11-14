package cc.efit.dispatch.biz.intelligent.strategy;

import cc.efit.call.api.vo.task.CallTaskConcurrency;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static cc.efit.call.api.constants.CallTaskPriority.PRIORITY_WEIGHTS;

@Slf4j
public class PriorityStrategy extends AbstractStrategy {
    private final int minConcurrency;
    public PriorityStrategy(int minConcurrency){
        this.minConcurrency = minConcurrency;
    }
    @Override
    protected int allocateCallTaskConcurrent(List<CallTaskConcurrency> taskList, int concurrency) {
        log.info("优先级策略分配开始");
        //分配最小并发
        taskInitConcurrent(taskList, minConcurrency);
        //剩余待分配
        int leftConcurrency = concurrency - minConcurrency * taskList.size();
        if (leftConcurrency <= 0) {
            log.info("分配最少并发之后都分配完了，不可思议");
            return 0;
        }
        allocateRemainingByWeight(taskList, leftConcurrency);
        int allocated = taskList.stream().mapToInt(CallTaskConcurrency::getConcurrency).sum();
        int remaining = concurrency - allocated;
        log.info("优先级比例 总并发数:{}, 已分配并发数:{}, 剩余并发数:{}", concurrency, allocated, remaining );
        return remaining;
    }


    private void allocateRemainingByWeight(List<CallTaskConcurrency> tasks, int availableConcurrency) {
        double totalWeight = tasks.stream()
                .mapToDouble(task -> PRIORITY_WEIGHTS.get(task.getPriority()))
                .sum();
        // 按权重分配
        for (CallTaskConcurrency task : tasks) {
            double weight = PRIORITY_WEIGHTS.get(task.getPriority());
            double proportion = weight / totalWeight;
            int additionalConcurrency = (int) Math.floor(availableConcurrency * proportion);
            task.setConcurrency(task.getConcurrency() + additionalConcurrency);
            task.setAssign(task.getConcurrency());
        }
    }
}
