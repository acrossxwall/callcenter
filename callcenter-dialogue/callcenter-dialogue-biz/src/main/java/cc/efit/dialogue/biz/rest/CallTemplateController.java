package cc.efit.dialogue.biz.rest;

import cc.efit.dialogue.biz.vo.template.TemplateFlowVo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.CallTemplate;
import cc.efit.dialogue.biz.service.CallTemplateService;
import cc.efit.dialogue.biz.service.dto.CallTemplateQueryCriteria;

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
import java.util.Collections;

/**
 * ai拨打话术Controller
 * 
 * @author across
 * @date 2025-08-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/template")
@Slf4j
public class CallTemplateController {

    private final CallTemplateService callTemplateService;

    /**
     * 查询ai拨打话术列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(CallTemplateQueryCriteria criteria, Pageable pageable) {
        return R.ok(callTemplateService.queryAll(criteria,pageable));
    }

    /**
     * 导出ai拨打话术列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("ai拨打话术导出" )
    @PostMapping("/export")
    public void exportCallTemplate(HttpServletResponse response, CallTemplateQueryCriteria criteria) throws IOException {
        callTemplateService.download(callTemplateService.queryAll(criteria), response);
    }

    /**
     * 获取ai拨打话术详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(callTemplateService.selectCallTemplateById(id));
    }

    /**
     * 新增ai拨打话术
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("ai拨打话术新增" )
    @PostMapping
    public R add(@RequestBody CallTemplate callTemplate) {
        log.info("创建模板:{}", callTemplate);
        return R.ok(callTemplateService.insertCallTemplate(callTemplate));
    }

    /**
     * 修改ai拨打话术
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("ai拨打话术修改" )
    @PutMapping
    public R edit(@RequestBody CallTemplate callTemplate) {
        callTemplateService.updateCallTemplate(callTemplate);
        return R.ok();
    }

    /**
     * 删除ai拨打话术
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("ai拨打话术删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        callTemplateService.deleteCallTemplateById(id);
        return R.ok();
    }

    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/flow/{id}")
    public R getTemplateNodeInfo(@PathVariable("id") Integer id) {
        return R.ok(callTemplateService.selectCallTemplateFlowByCallTemplateId(id));
    }
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @PostMapping(value = "/flow")
    public R saveTemplateNodeInfo(@RequestBody TemplateFlowVo flowVo) {
        log.info("saveTemplateNodeInfo:{}",flowVo);
        if (CollectionUtils.isEmpty(flowVo.nodes())) {
            return R.error("节点不能为空,请至少保留开场白");
        }
        callTemplateService.saveCallTemplateFlow(flowVo);
        return R.ok();
    }

    /**
     * 修改ai拨打话术
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("ai拨打话术提交审核" )
    @PostMapping("/review")
    public R review(@RequestBody CallTemplate callTemplate) {
        callTemplateService.reviewCallTemplate(callTemplate.getId());
        return R.ok();
    }
    @GetMapping("/reviewPass")
    public R getReviewPassTemplate() {
        return R.ok(callTemplateService.selectReviewPassTemplate());
    }
}
