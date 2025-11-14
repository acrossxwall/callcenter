package cc.efit.call.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.api.domain.CallRecord;
import cc.efit.call.biz.service.CallRecordService;
import cc.efit.call.biz.service.dto.CallRecordQueryCriteria;

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
 * 呼叫记录表Controller
 * 
 * @author across
 * @date 2025-09-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/record")
public class CallRecordController {

    private final CallRecordService callRecordService;

    /**
     * 查询呼叫记录表列表
     */
    @PreAuthorize("@cc.check('call:record:list')")
    @GetMapping("/list")
    public R list(CallRecordQueryCriteria criteria, Pageable pageable) {
        return R.ok(callRecordService.queryAll(criteria,pageable));
    }

    /**
     * 导出呼叫记录表列表
     */
    @PreAuthorize("@cc.check('call:record:export')")
    @Log("呼叫记录表导出" )
    @PostMapping("/export")
    public void exportCallRecord(HttpServletResponse response, CallRecordQueryCriteria criteria) throws IOException {
        callRecordService.download(callRecordService.queryAll(criteria), response);
    }

    /**
     * 获取呼叫记录表详细信息
     */
    @PreAuthorize("@cc.check('call:record:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callRecordService.selectCallRecordById(id));
    }

    /**
     * 新增呼叫记录表
     */
    @PreAuthorize("@cc.check('call:record:add')")
    @Log("呼叫记录表新增" )
    @PostMapping
    public R add(@RequestBody CallRecord callRecord) {
        callRecordService.insertCallRecord(callRecord);
        return R.ok();
    }

    /**
     * 修改呼叫记录表
     */
    @PreAuthorize("@cc.check('call:record:edit')")
    @Log("呼叫记录表修改" )
    @PutMapping
    public R edit(@RequestBody CallRecord callRecord) {
        callRecordService.updateCallRecord(callRecord);
        return R.ok();
    }

    /**
     * 删除呼叫记录表
     */
    @PreAuthorize("@cc.check('call:record:remove')")
    @Log("呼叫记录表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callRecordService.deleteCallRecordById(id);
        return R.ok();
    }
}
