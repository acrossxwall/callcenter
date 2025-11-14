package cc.efit.call.biz.rest;

import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.api.domain.Line;
import cc.efit.call.biz.service.LineService;
import cc.efit.call.biz.service.dto.LineQueryCriteria;

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
 * 中继线路网关表Controller
 * 
 * @author across
 * @date 2025-08-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/line")
public class LineController {

    private final LineService lineService;

    /**
     * 查询中继线路网关表列表
     */
    @PreAuthorize("@cc.check('call:line:list')")
    @GetMapping("/list")
    public R list(LineQueryCriteria criteria, Pageable pageable) {
        return R.ok(lineService.queryAll(criteria,pageable));
    }

    /**
     * 导出中继线路网关表列表
     */
    @PreAuthorize("@cc.check('call:line:export')")
    @Log("中继线路网关表导出" )
    @PostMapping("/export")
    public void exportLine(HttpServletResponse response, LineQueryCriteria criteria) throws IOException {
        lineService.download(lineService.queryAll(criteria), response);
    }

    /**
     * 获取中继线路网关表详细信息
     */
    @PreAuthorize("@cc.check('call:line:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(lineService.selectLineById(id));
    }

    /**
     * 新增中继线路网关表
     */
    @PreAuthorize("@cc.check('call:line:add')")
    @Log("中继线路网关表新增" )
    @PostMapping
    public R add(@RequestBody Line line) {
        lineService.insertLine(line);
        return R.ok();
    }

    /**
     * 修改中继线路网关表
     */
    @PreAuthorize("@cc.check('call:line:edit')")
    @Log("中继线路网关表修改" )
    @PutMapping
    public R edit(@RequestBody Line line) {
        lineService.updateLine(line);
        return R.ok();
    }

    /**
     * 删除中继线路网关表
     */
    @PreAuthorize("@cc.check('call:line:remove')")
    @Log("中继线路网关表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        lineService.deleteLineById(id);
        return R.ok();
    }
    @GetMapping("/assign")
    public R getAssignLineInfo() {
        return R.ok(lineService.findLineAssignInfo());
    }
}
