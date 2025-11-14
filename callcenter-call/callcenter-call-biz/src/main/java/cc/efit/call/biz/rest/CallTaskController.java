package cc.efit.call.biz.rest;

import cc.efit.call.api.vo.task.TaskStatisticsInfo;
import cc.efit.call.api.vo.task.TaskSummaryInfo;
import cc.efit.call.biz.service.CallTaskStatisticsService;
import cc.efit.dialogue.api.service.DialogueTemplateApi;
import cc.efit.call.api.enums.CallTaskEnum;
import cc.efit.call.api.domain.CallTask;
import cc.efit.call.biz.service.dto.CallTaskDto;
import cc.efit.res.R;
import cc.efit.web.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.biz.service.CallTaskService;
import cc.efit.call.biz.service.dto.CallTaskQueryCriteria;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cc.efit.annotation.Log;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

/**
 * 呼叫任务表Controller
 * 
 * @author across
 * @date 2025-08-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/task")
public class CallTaskController {

    private final CallTaskService callTaskService;

    private final DialogueTemplateApi dialogueTemplateApi;

    private final CallTaskStatisticsService callTaskStatisticsService;

    /**
     * 查询呼叫任务表列表
     */
    @PreAuthorize("@cc.check('call:task:list')")
    @GetMapping("/list")
    public R list(CallTaskQueryCriteria criteria, Pageable pageable) {
        return R.ok(callTaskService.queryAll(criteria,pageable));
    }

    /**
     * 导出呼叫任务表列表
     */
    @PreAuthorize("@cc.check('call:task:export')")
    @Log("呼叫任务表导出" )
    @PostMapping("/export")
    public void exportCallTask(HttpServletResponse response, CallTaskQueryCriteria criteria) throws IOException {
        callTaskService.download(callTaskService.queryAll(criteria), response);
    }

    /**
     * 获取呼叫任务表详细信息
     */
    @PreAuthorize("@cc.check('call:task:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callTaskService.selectCallTaskById(id));
    }

    /**
     * 新增呼叫任务表
     */
    @PreAuthorize("@cc.check('call:task:add')")
    @Log("呼叫任务表新增" )
    @PostMapping
    public R add(@RequestBody CallTask callTask) {
        callTaskService.insertCallTask(callTask,SecurityUtils. getCurrentDeptId());
        return R.ok();
    }

    /**
     * 修改呼叫任务表
     */
    @PreAuthorize("@cc.check('call:task:edit')")
    @Log("呼叫任务表修改" )
    @PutMapping
    public R edit(@RequestBody CallTask callTask) {
        callTaskService.updateCallTask(callTask, SecurityUtils. getCurrentDeptId());
        return R.ok();
    }

    /**
     * 删除呼叫任务表
     */
    @PreAuthorize("@cc.check('call:task:remove')")
    @Log("呼叫任务表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callTaskService.deleteCallTaskById(id);
        return R.ok();
    }

    @PostMapping("/switchTaskStatus")
    @PreAuthorize("@cc.check('call:task:edit')")
    @Log("呼叫任务修改状态" )
    public R switchTaskStatus(@RequestBody CallTask callTask) {
        callTaskService.switchTaskStatus(callTask);
        return R.ok();
    }
    @PreAuthorize("@cc.check('call:task:query')")
    @GetMapping(value = "/allInfo")
    public R getAllProcessingTask(){
        return R.ok(callTaskService.findAllProcessingTask(CallTaskEnum.TaskStatus.ENABLE.getStatus()));
    }

    @PreAuthorize("@cc.check('call:task:query')")
    @GetMapping(value = "/customer/dict/{id}")
    public R getTaskCallTemplateCustomerDict(@PathVariable("id") Integer id) {
        CallTaskDto task = callTaskService.selectCallTaskById(id);
        if (task == null) {
            return R.error("任务不存在");
        }
        return R.ok(dialogueTemplateApi.getCallTemplateCustomerDict(task.getCallTemplateId()));
    }
    @GetMapping(value = "/summary")
    public R getTaskCallSummaryInfo() {
        Long count = callTaskService.getTaskCountInfo();
        TaskStatisticsInfo info = callTaskStatisticsService.selectCallTaskStatisticsInfo();
        return R.ok(new TaskSummaryInfo(count, info));
    }
    @GetMapping(value = "/status/count")
    public R getTaskCallStatusCountInfo() {
        return R.ok(callTaskService.getTaskCallStatusCountInfo());
    }
}
