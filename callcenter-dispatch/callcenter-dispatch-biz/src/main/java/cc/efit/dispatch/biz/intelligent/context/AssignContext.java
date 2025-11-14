package cc.efit.dispatch.biz.intelligent.context;


public record AssignContext (
    Integer taskId,
    Integer newConcurrency,
    Integer lastConcurrency,
    Integer newLineId,
    Integer oldLineId,
    boolean firstAssign,
    boolean concurrencyChanged,
    boolean lineChanged
){}