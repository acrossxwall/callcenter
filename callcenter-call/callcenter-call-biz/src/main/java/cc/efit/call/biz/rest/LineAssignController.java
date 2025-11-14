package cc.efit.call.biz.rest;

import cc.efit.call.biz.vo.line.AssignLineVo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.api.domain.LineAssign;
import cc.efit.call.biz.service.LineAssignService;
import cc.efit.call.biz.service.dto.LineAssignQueryCriteria;

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
 * 线路分配表Controller
 * 
 * @author across
 * @date 2025-08-28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/assign")
public class LineAssignController {

    private final LineAssignService lineAssignService;

    /**
     * 查询线路分配表列表
     */
    @PreAuthorize("@cc.check('call:assign:list')")
    @GetMapping("/list")
    public R list(LineAssignQueryCriteria criteria, Pageable pageable) {
        return R.ok(lineAssignService.queryAll(criteria,pageable));
    }

    /**
     * 导出线路分配表列表
     */
    @PreAuthorize("@cc.check('call:assign:export')")
    @Log("线路分配表导出" )
    @PostMapping("/export")
    public void exportLineAssign(HttpServletResponse response, LineAssignQueryCriteria criteria) throws IOException {
        lineAssignService.download(lineAssignService.queryAll(criteria), response);
    }

    /**
     * 获取线路分配表详细信息
     */
    @PreAuthorize("@cc.check('call:assign:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(lineAssignService.selectLineAssignById(id));
    }

    /**
     * 新增线路分配表
     */
    @PreAuthorize("@cc.check('call:assign:add')")
    @Log("线路分配表新增" )
    @PostMapping
    public R add(@RequestBody LineAssign lineAssign) {
        lineAssignService.insertLineAssign(lineAssign);
        return R.ok();
    }

    /**
     * 修改线路分配表
     */
    @PreAuthorize("@cc.check('call:assign:edit')")
    @Log("线路分配表修改" )
    @PutMapping
    public R edit(@RequestBody LineAssign lineAssign) {
        lineAssignService.updateLineAssign(lineAssign);
        return R.ok();
    }

    /**
     * 删除线路分配表
     */
    @PreAuthorize("@cc.check('call:assign:remove')")
    @Log("线路分配表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        lineAssignService.deleteLineAssignById(id);
        return R.ok();
    }
    @PreAuthorize("@cc.check('call:line:edit')")
    @Log("线路分配" )
    @PostMapping("/line")
    public R assignLineInfo(@RequestBody AssignLineVo vo) {
        lineAssignService.assignLineInfo(vo);
        return R.ok();
    }

    @PreAuthorize("@cc.check('call:line:edit')")
    @GetMapping("/lineId/{id}")
    public R getAssignLineInfo(@PathVariable("id")Integer lineId) {
        return R.ok(lineAssignService.getAssignLineInfo(lineId));
    }
}
