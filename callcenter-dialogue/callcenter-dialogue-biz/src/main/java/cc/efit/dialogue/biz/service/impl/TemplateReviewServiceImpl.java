package cc.efit.dialogue.biz.service.impl;

import cc.efit.dialogue.api.enums.CallTemplateStatusEnum;
import cc.efit.dialogue.biz.repository.CallTemplateRepository;
import cc.efit.dialogue.biz.vo.review.TemplateReviewInfo;
import cc.efit.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateReviewMapper;
import cc.efit.dialogue.biz.repository.TemplateReviewRepository;
import cc.efit.dialogue.biz.service.dto.TemplateReviewDto;
import cc.efit.dialogue.biz.service.dto.TemplateReviewQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateReview;
import cc.efit.dialogue.biz.service.TemplateReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 话术审核记录Service业务层处理
 * 
 * @author across
 * @date 2025-08-26
 */
@Service
@RequiredArgsConstructor
public class TemplateReviewServiceImpl implements TemplateReviewService  {

    private final TemplateReviewRepository templateReviewRepository;
    private final TemplateReviewMapper templateReviewMapper;
    private final CallTemplateRepository callTemplateRepository;

    @Override
    public PageResult<TemplateReviewDto> queryAll(TemplateReviewQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateReview> page = templateReviewRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateReviewMapper::toDto));
    }

    @Override
    public List<TemplateReviewDto> queryAll(TemplateReviewQueryCriteria criteria){
        return templateReviewMapper.toDto(templateReviewRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询话术审核记录
     * 
     * @param id 话术审核记录主键
     * @return 话术审核记录
     */
    @Override
    public TemplateReviewDto selectTemplateReviewById(Integer id)  {
        return templateReviewMapper.toDto(templateReviewRepository.findById(id).orElseGet(TemplateReview::new));
    }


    /**
     * 新增话术审核记录
     * 
     * @param templateReview 话术审核记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateReview(TemplateReview templateReview) {
        templateReviewRepository.save(templateReview);
    }

    /**
     * 修改话术审核记录
     * 
     * @param templateReview 话术审核记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateReview(TemplateReview templateReview) {
        templateReviewRepository.save(templateReview);
    }

    /**
     * 批量删除话术审核记录
     * 
     * @param ids 需要删除的话术审核记录主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateReviewByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateReviewById(id);
        }
    }

    /**
     * 删除话术审核记录信息
     * 
     * @param id 话术审核记录主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateReviewById(Integer id) {
        templateReviewRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateReviewDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateReviewDto templateReview : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateReview.getId());
            map.put("callTemplateId",  templateReview.getCallTemplateId());
            map.put("remark",  templateReview.getRemark());
            map.put("name",  templateReview.getName());
            map.put("status",  templateReview.getStatus());
            map.put("createBy",  templateReview.getCreateBy());
            map.put("updateBy",  templateReview.getUpdateBy());
            map.put("createTime",  templateReview.getCreateTime());
            map.put("deleted",  templateReview.getDeleted());
            map.put("updateTime",  templateReview.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewTemplate(TemplateReviewInfo reviewInfo) {
        if (CallTemplateStatusEnum.REJECT.getStatus().equals(reviewInfo.status()) && StringUtils.isBlank(reviewInfo.remark())) {
            throw new BadRequestException("审核不通过时，请填写理由");
        }
        String remark = CallTemplateStatusEnum.REJECT.getStatus().equals(reviewInfo.status())? reviewInfo.remark() : "";
        templateReviewRepository.reviewTemplateByCallTemplateId(reviewInfo.callTemplateId(), reviewInfo.status(), remark, LocalDateTime.now());
        callTemplateRepository.updateTemplateStatusById(reviewInfo.callTemplateId(), reviewInfo.status());
    }

}
