package cc.efit.call.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.biz.domain.CallCustomerImportDetail;
import cc.efit.call.biz.service.CallCustomerImportDetailService;
import cc.efit.call.biz.service.dto.CallCustomerImportDetailQueryCriteria;

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
 * 客户名单批次详细表Controller
 * 
 * @author across
 * @date 2025-09-12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/detail")
public class CallCustomerImportDetailController {

    private final CallCustomerImportDetailService callCustomerImportDetailService;

    /**
     * 查询客户名单批次详细表列表
     */
    @PreAuthorize("@cc.check('call:detail:list')")
    @GetMapping("/list")
    public R list(CallCustomerImportDetailQueryCriteria criteria, Pageable pageable) {
        return R.ok(callCustomerImportDetailService.queryAll(criteria,pageable));
    }

    /**
     * 导出客户名单批次详细表列表
     */
    @PreAuthorize("@cc.check('call:detail:export')")
    @Log("客户名单批次详细表导出" )
    @PostMapping("/export")
    public void exportCallCustomerImportDetail(HttpServletResponse response, CallCustomerImportDetailQueryCriteria criteria) throws IOException {
        callCustomerImportDetailService.download(callCustomerImportDetailService.queryAll(criteria), response);
    }

    /**
     * 获取客户名单批次详细表详细信息
     */
    @PreAuthorize("@cc.check('call:detail:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callCustomerImportDetailService.selectCallCustomerImportDetailById(id));
    }

    /**
     * 新增客户名单批次详细表
     */
    @PreAuthorize("@cc.check('call:detail:add')")
    @Log("客户名单批次详细表新增" )
    @PostMapping
    public R add(@RequestBody CallCustomerImportDetail callCustomerImportDetail) {
        callCustomerImportDetailService.insertCallCustomerImportDetail(callCustomerImportDetail);
        return R.ok();
    }

    /**
     * 修改客户名单批次详细表
     */
    @PreAuthorize("@cc.check('call:detail:edit')")
    @Log("客户名单批次详细表修改" )
    @PutMapping
    public R edit(@RequestBody CallCustomerImportDetail callCustomerImportDetail) {
        callCustomerImportDetailService.updateCallCustomerImportDetail(callCustomerImportDetail);
        return R.ok();
    }

    /**
     * 删除客户名单批次详细表
     */
    @PreAuthorize("@cc.check('call:detail:remove')")
    @Log("客户名单批次详细表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callCustomerImportDetailService.deleteCallCustomerImportDetailById(id);
        return R.ok();
    }
}
