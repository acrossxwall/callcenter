package cc.efit.call.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.api.domain.CallTaskStatistics;
import cc.efit.call.biz.service.CallTaskStatisticsService;
import cc.efit.call.biz.service.dto.CallTaskStatisticsQueryCriteria;

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
 * 呼叫任务统计表表Controller
 * 
 * @author across
 * @date 2025-10-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/statistics")
public class CallTaskStatisticsController {

    private final CallTaskStatisticsService callTaskStatisticsService;

    /**
     * 查询呼叫任务统计表表列表
     */
    @PreAuthorize("@cc.check('call:statistics:list')")
    @GetMapping("/list")
    public R list(CallTaskStatisticsQueryCriteria criteria, Pageable pageable) {
        return R.ok(callTaskStatisticsService.queryAll(criteria,pageable));
    }

    /**
     * 导出呼叫任务统计表表列表
     */
    @PreAuthorize("@cc.check('call:statistics:export')")
    @Log("呼叫任务统计表表导出" )
    @PostMapping("/export")
    public void exportCallTaskStatistics(HttpServletResponse response, CallTaskStatisticsQueryCriteria criteria) throws IOException {
        callTaskStatisticsService.download(callTaskStatisticsService.queryAll(criteria), response);
    }

    /**
     * 获取呼叫任务统计表表详细信息
     */
    @PreAuthorize("@cc.check('call:statistics:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callTaskStatisticsService.selectCallTaskStatisticsById(id));
    }

    /**
     * 新增呼叫任务统计表表
     */
    @PreAuthorize("@cc.check('call:statistics:add')")
    @Log("呼叫任务统计表表新增" )
    @PostMapping
    public R add(@RequestBody CallTaskStatistics callTaskStatistics) {
        callTaskStatisticsService.insertCallTaskStatistics(callTaskStatistics);
        return R.ok();
    }

    /**
     * 修改呼叫任务统计表表
     */
    @PreAuthorize("@cc.check('call:statistics:edit')")
    @Log("呼叫任务统计表表修改" )
    @PutMapping
    public R edit(@RequestBody CallTaskStatistics callTaskStatistics) {
        callTaskStatisticsService.updateCallTaskStatistics(callTaskStatistics);
        return R.ok();
    }

    /**
     * 删除呼叫任务统计表表
     */
    @PreAuthorize("@cc.check('call:statistics:remove')")
    @Log("呼叫任务统计表表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callTaskStatisticsService.deleteCallTaskStatisticsById(id);
        return R.ok();
    }
    @GetMapping("/info")
    public R getTaskStatisticsInfo() {
        return R.ok(callTaskStatisticsService.selectCallTaskStatisticsInfo());
    }
}
