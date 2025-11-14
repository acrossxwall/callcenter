package cc.efit.call.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.call.biz.service.CallSystemStatisticsService;
import cc.efit.call.biz.service.dto.CallSystemStatisticsQueryCriteria;

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
 * 呼叫任务系统维度统计表Controller
 * 
 * @author across
 * @date 2025-10-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/system")
public class CallSystemStatisticsController {

    private final CallSystemStatisticsService callSystemStatisticsService;

    /**
     * 查询呼叫任务系统维度统计表列表
     */
    @PreAuthorize("@cc.check('call:system:list')")
    @GetMapping("/list")
    public R list(CallSystemStatisticsQueryCriteria criteria, Pageable pageable) {
        return R.ok(callSystemStatisticsService.queryAll(criteria,pageable));
    }

    /**
     * 导出呼叫任务系统维度统计表列表
     */
    @PreAuthorize("@cc.check('call:system:export')")
    @Log("呼叫任务系统维度统计表导出" )
    @PostMapping("/export")
    public void exportCallSystemStatistics(HttpServletResponse response, CallSystemStatisticsQueryCriteria criteria) throws IOException {
        callSystemStatisticsService.download(callSystemStatisticsService.queryAll(criteria), response);
    }

    /**
     * 获取呼叫任务系统维度统计表详细信息
     */
    @PreAuthorize("@cc.check('call:system:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callSystemStatisticsService.selectCallSystemStatisticsById(id));
    }

    /**
     * 新增呼叫任务系统维度统计表
     */
    @PreAuthorize("@cc.check('call:system:add')")
    @Log("呼叫任务系统维度统计表新增" )
    @PostMapping
    public R add(@RequestBody CallSystemStatistics callSystemStatistics) {
        callSystemStatisticsService.insertCallSystemStatistics(callSystemStatistics);
        return R.ok();
    }

    /**
     * 修改呼叫任务系统维度统计表
     */
    @PreAuthorize("@cc.check('call:system:edit')")
    @Log("呼叫任务系统维度统计表修改" )
    @PutMapping
    public R edit(@RequestBody CallSystemStatistics callSystemStatistics) {
        callSystemStatisticsService.updateCallSystemStatistics(callSystemStatistics);
        return R.ok();
    }

    /**
     * 删除呼叫任务系统维度统计表
     */
    @PreAuthorize("@cc.check('call:system:remove')")
    @Log("呼叫任务系统维度统计表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callSystemStatisticsService.deleteCallSystemStatisticsById(id);
        return R.ok();
    }

    @GetMapping("/statistics")
    public R getSystemStatisticsInfo(){
        return R.ok(callSystemStatisticsService.getSystemStatisticsInfo());
    }
}
