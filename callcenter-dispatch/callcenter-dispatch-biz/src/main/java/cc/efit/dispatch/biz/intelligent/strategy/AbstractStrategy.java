package cc.efit.dispatch.biz.intelligent.strategy;

import cc.efit.call.api.vo.task.CallTaskConcurrency;
import cc.efit.dispatch.biz.intelligent.AllocationStrategy;

import java.util.List;

public abstract class AbstractStrategy implements AllocationStrategy  {

    protected  abstract  int allocateCallTaskConcurrent(List<CallTaskConcurrency> taskList, int concurrency);

    private boolean checkSetConcurrency(List<CallTaskConcurrency> tasks, int concurrency  ) {
        if (tasks.size()==1) {
            CallTaskConcurrency task = tasks.getFirst();
            int min = Math.min(concurrency,task.getLineConcurrent());
            task.setConcurrency(min);
            task.setAssign(min);
            return true;
        }
        int setConcurrent = tasks.stream().mapToInt(CallTaskConcurrency::getLineConcurrent).sum();
        if (setConcurrent<=concurrency) {
            for (CallTaskConcurrency task : tasks) {
                task.setConcurrency(task.getLineConcurrent());
                task.setAssign(task.getLineConcurrent());
            }
            return true;
        }
        return false;
    }

    private void remainingConcurrencyAllocation(List<CallTaskConcurrency> taskList, int remaining ) {
        while (remaining > 0) {
            boolean allocated = false;
            for (CallTaskConcurrency task : taskList) {
//                if (recallTaskAlreadyAllocate(task)) {
//                    continue;
//                }
                if (remaining == 0) {
                    break;
                }
                if (task.getConcurrency() < task.getLineConcurrent()) {
                    task.setConcurrency(task.getConcurrency() + 1);
                    task.setAssign(task.getConcurrency());
                    remaining--;
                    allocated = true;
                }
            }
            if (!allocated) {
                break;
            }
        }
    }

    protected void taskInitConcurrent(List<CallTaskConcurrency> callTaskList, int minConcurrency) {
        callTaskList.forEach(s->s.setConcurrency(minConcurrency));
    }

    @Override
    public boolean allocateCallTask(List<CallTaskConcurrency> taskList, int concurrency) {
        if (checkSetConcurrency(taskList, concurrency)) {
            return true;
        }
        int balance = allocateCallTaskConcurrent(taskList, concurrency);
        if (balance>0) {
            remainingConcurrencyAllocation(taskList, balance);
        }
        return false;
    }
}
