package cc.efit.dialogue.biz.service.impl;

import cc.efit.dialogue.api.enums.TemplateIntentionTypeEnum;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import cc.efit.dialogue.biz.vo.intention.IntentionInfo;
import cc.efit.enums.CommonOperatorEnum;
import cc.efit.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateIntentionMapper;
import cc.efit.dialogue.biz.repository.TemplateIntentionRepository;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionDto;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateIntention;
import cc.efit.dialogue.biz.service.TemplateIntentionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 意图分支Service业务层处理
 * 
 * @author across
 * @date 2025-08-14
 */
@Service
@RequiredArgsConstructor
public class TemplateIntentionServiceImpl implements TemplateIntentionService  {

    private final TemplateIntentionRepository templateIntentionRepository;
    private final TemplateIntentionMapper templateIntentionMapper;
    private final CallTemplateOperatorService callTemplateOperatorService;
    @Override
    public PageResult<TemplateIntentionDto> queryAll(TemplateIntentionQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateIntention> page = templateIntentionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateIntentionMapper::toDto));
    }

    @Override
    public List<TemplateIntentionDto> queryAll(TemplateIntentionQueryCriteria criteria){
        return templateIntentionMapper.toDto(templateIntentionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询意图分支
     * 
     * @param id 意图分支主键
     * @return 意图分支
     */
    @Override
    public TemplateIntentionDto selectTemplateIntentionById(Integer id)  {
        return templateIntentionMapper.toDto(templateIntentionRepository.findById(id).orElseGet(TemplateIntention::new));
    }


    /**
     * 新增意图分支
     * 
     * @param templateIntention 意图分支
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertTemplateIntention(TemplateIntention templateIntention) {
        validTemplateIntentionName(templateIntention);
        templateIntention.setType(TemplateIntentionTypeEnum.NORMAL.getCode());
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateIntention.getCallTemplateId(), CommonOperatorEnum.ADD.getCode(), "新增意图："+templateIntention.getName());
        templateIntentionRepository.save(templateIntention);
        return templateIntention.getId();
    }

    /**
     * 修改意图分支
     * 
     * @param templateIntention 意图分支
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateIntention(TemplateIntention templateIntention) {
        validTemplateIntentionName(templateIntention);
        TemplateIntention intention = templateIntentionRepository.findById(templateIntention.getId()).orElseGet(TemplateIntention::new);
        if (intention.getId()==null) {
            throw new BadRequestException("意图不存在");
        }
        intention.setName(templateIntention.getName());
        intention.setSort(templateIntention.getSort());
        intention.setKeywords(templateIntention.getKeywords());
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateIntention.getCallTemplateId(), CommonOperatorEnum.ADD.getCode(), "修改意图："+templateIntention.getName());
        templateIntentionRepository.save(intention);
    }

    /**
     * 批量删除意图分支
     * 
     * @param ids 需要删除的意图分支主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateIntentionByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateIntentionById(id);
        }
    }

    /**
     * 删除意图分支信息
     * 
     * @param id 意图分支主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateIntentionById(Integer id) {
        TemplateIntention templateIntention = templateIntentionRepository.findById(id).orElse(null);
        if (templateIntention==null) {
            return;
        }
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateIntention.getCallTemplateId(), CommonOperatorEnum.ADD.getCode(), "删除意图："+templateIntention.getName());
        templateIntentionRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateIntentionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateIntentionDto templateIntention : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateIntention.getId());
            map.put("callTemplateId",  templateIntention.getCallTemplateId());
            map.put("name",  templateIntention.getName());
            map.put("keywords",  templateIntention.getKeywords());
            map.put("sort",  templateIntention.getSort());
            map.put("createBy",  templateIntention.getCreateBy());
            map.put("updateBy",  templateIntention.getUpdateBy());
            map.put("createTime",  templateIntention.getCreateTime());
            map.put("deleted",  templateIntention.getDeleted());
            map.put("updateTime",  templateIntention.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<IntentionInfo> selectTemplateIntentionByCallTemplateId(Integer callTemplateId) {
        List<TemplateIntention> list = templateIntentionRepository.findByCallTemplateIdOrderBySortAsc(callTemplateId);
        return list.stream().map(s-> new IntentionInfo(s.getId(),s.getName(),s.getType())).toList();
    }

    private void validTemplateIntentionName( TemplateIntention templateIntention) {
        String name = templateIntention.getName();
        TemplateIntention intention = templateIntentionRepository.findByNameAndCallTemplateId(name,templateIntention.getCallTemplateId());
        if (intention==null) {
            return ;
        }
        if (templateIntention.getId()==null || !intention.getId().equals(templateIntention.getId())) {
            throw new BadRequestException("意图名称重复");
        }
    }

}
