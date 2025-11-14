package cc.efit.call.api.vo.task;

public record CallTaskInfo(
        Integer id,
        Integer callTemplateId,
        String taskName
) {
}
