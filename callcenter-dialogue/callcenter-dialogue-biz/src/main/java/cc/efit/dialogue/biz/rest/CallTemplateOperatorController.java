package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.CallTemplateOperator;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import cc.efit.dialogue.biz.service.dto.CallTemplateOperatorQueryCriteria;

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
 * 话术模板操作日志Controller
 * 
 * @author across
 * @date 2025-08-12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/operator")
public class CallTemplateOperatorController {

    private final CallTemplateOperatorService callTemplateOperatorService;

    /**
     * 查询话术模板操作日志列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(CallTemplateOperatorQueryCriteria criteria, Pageable pageable) {
        return R.ok(callTemplateOperatorService.queryAll(criteria,pageable));
    }

    /**
     * 导出话术模板操作日志列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("话术模板操作日志导出" )
    @PostMapping("/export")
    public void exportCallTemplateOperator(HttpServletResponse response, CallTemplateOperatorQueryCriteria criteria) throws IOException {
        callTemplateOperatorService.download(callTemplateOperatorService.queryAll(criteria), response);
    }

    /**
     * 获取话术模板操作日志详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callTemplateOperatorService.selectCallTemplateOperatorById(id));
    }

    /**
     * 新增话术模板操作日志
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("话术模板操作日志新增" )
    @PostMapping
    public R add(@RequestBody CallTemplateOperator callTemplateOperator) {
        callTemplateOperatorService.insertCallTemplateOperator(callTemplateOperator);
        return R.ok();
    }

    /**
     * 修改话术模板操作日志
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("话术模板操作日志修改" )
    @PutMapping
    public R edit(@RequestBody CallTemplateOperator callTemplateOperator) {
        callTemplateOperatorService.updateCallTemplateOperator(callTemplateOperator);
        return R.ok();
    }

    /**
     * 删除话术模板操作日志
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("话术模板操作日志删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callTemplateOperatorService.deleteCallTemplateOperatorById(id);
        return R.ok();
    }
}
