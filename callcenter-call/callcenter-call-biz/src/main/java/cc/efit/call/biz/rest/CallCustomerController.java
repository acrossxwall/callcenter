package cc.efit.call.biz.rest;

import cc.efit.call.api.enums.ImportCustomerTypeEnum;
import cc.efit.call.biz.vo.customer.ImportCustomerInfo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.api.domain.CallCustomer;
import cc.efit.call.biz.service.CallCustomerService;
import cc.efit.call.biz.service.dto.CallCustomerQueryCriteria;

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
 * 客户名单表Controller
 * 
 * @author across
 * @date 2025-09-10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/customer")
public class CallCustomerController {

    private final CallCustomerService callCustomerService;

    /**
     * 查询客户名单表列表
     */
    @PreAuthorize("@cc.check('call:customer:list')")
    @GetMapping("/list")
    public R list(CallCustomerQueryCriteria criteria, Pageable pageable) {
        return R.ok(callCustomerService.queryAll(criteria,pageable));
    }

    /**
     * 导出客户名单表列表
     */
    @PreAuthorize("@cc.check('call:customer:export')")
    @Log("客户名单表导出" )
    @PostMapping("/export")
    public void exportCallCustomer(HttpServletResponse response, CallCustomerQueryCriteria criteria) throws IOException {
        callCustomerService.download(callCustomerService.queryAll(criteria), response);
    }

    /**
     * 获取客户名单表详细信息
     */
    @PreAuthorize("@cc.check('call:customer:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callCustomerService.selectCallCustomerById(id));
    }

    /**
     * 新增客户名单表
     */
    @PreAuthorize("@cc.check('call:customer:add')")
    @Log("客户名单表新增" )
    @PostMapping
    public R add(@RequestBody CallCustomer callCustomer) {
        callCustomerService.insertCallCustomer(callCustomer);
        return R.ok();
    }

    /**
     * 修改客户名单表
     */
    @PreAuthorize("@cc.check('call:customer:edit')")
    @Log("客户名单表修改" )
    @PutMapping
    public R edit(@RequestBody CallCustomer callCustomer) {
        callCustomerService.updateCallCustomer(callCustomer);
        return R.ok();
    }

    /**
     * 删除客户名单表
     */
    @PreAuthorize("@cc.check('call:customer:remove')")
    @Log("客户名单表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callCustomerService.deleteCallCustomerById(id);
        return R.ok();
    }

    @PostMapping("/import")
    @PreAuthorize("@cc.check('call:task:edit')")
    public R uploadCallTaskCustomer(@RequestBody ImportCustomerInfo importReq) {
        if (importReq.taskId()==null) {
            return R.error("拨打任务不能为空");
        }
        if (importReq.importType()==null) {
            return R.error("导入类型不能为空");
        }
        if ( (ImportCustomerTypeEnum.EXCEL.getType().equals(importReq.importType()) && importReq.fileId()==null)
            || (ImportCustomerTypeEnum.MANUAL.getType().equals(importReq.importType()) && CollectionUtils.isEmpty(importReq.customers())) ) {
            return R.error("导入文件或者手动输入号码不能为空");
        }
        callCustomerService.importCustomer(importReq);
        return R.ok();
    }
}
