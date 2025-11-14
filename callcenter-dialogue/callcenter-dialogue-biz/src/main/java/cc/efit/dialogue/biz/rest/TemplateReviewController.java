package cc.efit.dialogue.biz.rest;

import cc.efit.dialogue.biz.vo.review.TemplateReviewInfo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateReview;
import cc.efit.dialogue.biz.service.TemplateReviewService;
import cc.efit.dialogue.biz.service.dto.TemplateReviewQueryCriteria;

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
 * 话术审核记录Controller
 * 
 * @author across
 * @date 2025-08-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/review")
public class TemplateReviewController {

    private final TemplateReviewService templateReviewService;

    /**
     * 查询话术审核记录列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateReviewQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateReviewService.queryAll(criteria,pageable));
    }

    /**
     * 导出话术审核记录列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("话术审核记录导出" )
    @PostMapping("/export")
    public void exportTemplateReview(HttpServletResponse response, TemplateReviewQueryCriteria criteria) throws IOException {
        templateReviewService.download(templateReviewService.queryAll(criteria), response);
    }

    /**
     * 获取话术审核记录详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateReviewService.selectTemplateReviewById(id));
    }

    /**
     * 新增话术审核记录
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("话术审核记录新增" )
    @PostMapping
    public R add(@RequestBody TemplateReview templateReview) {
        templateReviewService.insertTemplateReview(templateReview);
        return R.ok();
    }

    /**
     * 修改话术审核记录
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("话术审核记录修改" )
    @PutMapping
    public R edit(@RequestBody TemplateReview templateReview) {
        templateReviewService.updateTemplateReview(templateReview);
        return R.ok();
    }

    /**
     * 删除话术审核记录
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("话术审核记录删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateReviewService.deleteTemplateReviewById(id);
        return R.ok();
    }
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @PostMapping("/template")
    public R reviewTemplate(@RequestBody TemplateReviewInfo reviewInfo) {
        templateReviewService.reviewTemplate(reviewInfo);
        return R.ok();
    }
}
