package cc.efit.dispatch.biz.intelligent;

import cc.efit.call.api.vo.task.CallTaskConcurrency;

import java.util.List;

public interface AllocationStrategy {

    boolean allocateCallTask(List<CallTaskConcurrency> taskList,int concurrency);

}
