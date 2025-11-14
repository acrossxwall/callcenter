package cc.efit.call.biz.rest;

import cc.efit.call.biz.domain.BlackInfo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.call.biz.service.BlackInfoService;
import cc.efit.call.biz.service.dto.BlackInfoQueryCriteria;

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
 * 黑名单库表Controller
 * 
 * @author across
 * @date 2025-08-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call/black")
public class BlackInfoController {

    private final BlackInfoService blackInfoService;

    /**
     * 查询黑名单库表列表
     */
    @PreAuthorize("@cc.check('call:black:list')")
    @GetMapping("/list")
    public R list(BlackInfoQueryCriteria criteria, Pageable pageable) {
        return R.ok(blackInfoService.queryAll(criteria,pageable));
    }

    /**
     * 导出黑名单库表列表
     */
    @PreAuthorize("@cc.check('call:black:export')")
    @Log("黑名单库表导出" )
    @PostMapping("/export")
    public void exportBlackInfo(HttpServletResponse response, BlackInfoQueryCriteria criteria) throws IOException {
        blackInfoService.download(blackInfoService.queryAll(criteria), response);
    }

    /**
     * 获取黑名单库表详细信息
     */
    @PreAuthorize("@cc.check('call:black:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(blackInfoService.selectBlackInfoById(id));
    }

    /**
     * 新增黑名单库表
     */
    @PreAuthorize("@cc.check('call:black:add')")
    @Log("黑名单库表新增" )
    @PostMapping
    public R add(@RequestBody BlackInfo blackInfo) {
        blackInfoService.insertBlackInfo(blackInfo);
        return R.ok();
    }

    /**
     * 修改黑名单库表
     */
    @PreAuthorize("@cc.check('call:black:edit')")
    @Log("黑名单库表修改" )
    @PutMapping
    public R edit(@RequestBody BlackInfo blackInfo) {
        blackInfoService.updateBlackInfo(blackInfo);
        return R.ok();
    }

    /**
     * 删除黑名单库表
     */
    @PreAuthorize("@cc.check('call:black:remove')")
    @Log("黑名单库表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        blackInfoService.deleteBlackInfoById(id);
        return R.ok();
    }
}
