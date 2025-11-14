package cc.efit.call.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.biz.domain.CallTaskJob;
import cc.efit.call.biz.service.CallTaskJobService;
import cc.efit.call.biz.service.dto.CallTaskJobQueryCriteria;

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
 * 外呼任务job表Controller
 * 
 * @author across
 * @date 2025-10-20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/job")
public class CallTaskJobController {

    private final CallTaskJobService callTaskJobService;

    /**
     * 查询外呼任务job表列表
     */
    @PreAuthorize("@cc.check('call:job:list')")
    @GetMapping("/list")
    public R list(CallTaskJobQueryCriteria criteria, Pageable pageable) {
        return R.ok(callTaskJobService.queryAll(criteria,pageable));
    }

    /**
     * 导出外呼任务job表列表
     */
    @PreAuthorize("@cc.check('call:job:export')")
    @Log("外呼任务job表导出" )
    @PostMapping("/export")
    public void exportCallTaskJob(HttpServletResponse response, CallTaskJobQueryCriteria criteria) throws IOException {
        callTaskJobService.download(callTaskJobService.queryAll(criteria), response);
    }

    /**
     * 获取外呼任务job表详细信息
     */
    @PreAuthorize("@cc.check('call:job:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callTaskJobService.selectCallTaskJobById(id));
    }

    /**
     * 新增外呼任务job表
     */
    @PreAuthorize("@cc.check('call:job:add')")
    @Log("外呼任务job表新增" )
    @PostMapping
    public R add(@RequestBody CallTaskJob callTaskJob) {
        callTaskJobService.insertCallTaskJob(callTaskJob);
        return R.ok();
    }

    /**
     * 修改外呼任务job表
     */
    @PreAuthorize("@cc.check('call:job:edit')")
    @Log("外呼任务job表修改" )
    @PutMapping
    public R edit(@RequestBody CallTaskJob callTaskJob) {
        callTaskJobService.updateCallTaskJob(callTaskJob);
        return R.ok();
    }

    /**
     * 删除外呼任务job表
     */
    @PreAuthorize("@cc.check('call:job:remove')")
    @Log("外呼任务job表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callTaskJobService.deleteCallTaskJobById(id);
        return R.ok();
    }
}
