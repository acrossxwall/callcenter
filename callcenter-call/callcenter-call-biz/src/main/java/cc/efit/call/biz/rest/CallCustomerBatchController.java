package cc.efit.call.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.biz.domain.CallCustomerBatch;
import cc.efit.call.biz.service.CallCustomerBatchService;
import cc.efit.call.biz.service.dto.CallCustomerBatchQueryCriteria;

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
 * 客户名单批次表Controller
 * 
 * @author across
 * @date 2025-09-10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/batch")
public class CallCustomerBatchController {

    private final CallCustomerBatchService callCustomerBatchService;

    /**
     * 查询客户名单批次表列表
     */
    @PreAuthorize("@cc.check('call:batch:list')")
    @GetMapping("/list")
    public R list(CallCustomerBatchQueryCriteria criteria, Pageable pageable) {
        return R.ok(callCustomerBatchService.queryAll(criteria,pageable));
    }

    /**
     * 导出客户名单批次表列表
     */
    @PreAuthorize("@cc.check('call:batch:export')")
    @Log("客户名单批次表导出" )
    @PostMapping("/export")
    public void exportCallCustomerBatch(HttpServletResponse response, CallCustomerBatchQueryCriteria criteria) throws IOException {
        callCustomerBatchService.download(callCustomerBatchService.queryAll(criteria), response);
    }

    /**
     * 获取客户名单批次表详细信息
     */
    @PreAuthorize("@cc.check('call:batch:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callCustomerBatchService.selectCallCustomerBatchById(id));
    }

    /**
     * 新增客户名单批次表
     */
    @PreAuthorize("@cc.check('call:batch:add')")
    @Log("客户名单批次表新增" )
    @PostMapping
    public R add(@RequestBody CallCustomerBatch callCustomerBatch) {
        callCustomerBatchService.insertCallCustomerBatch(callCustomerBatch);
        return R.ok();
    }

    /**
     * 修改客户名单批次表
     */
    @PreAuthorize("@cc.check('call:batch:edit')")
    @Log("客户名单批次表修改" )
    @PutMapping
    public R edit(@RequestBody CallCustomerBatch callCustomerBatch) {
        callCustomerBatchService.updateCallCustomerBatch(callCustomerBatch);
        return R.ok();
    }

    /**
     * 删除客户名单批次表
     */
    @PreAuthorize("@cc.check('call:batch:remove')")
    @Log("客户名单批次表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callCustomerBatchService.deleteCallCustomerBatchById(id);
        return R.ok();
    }
}
