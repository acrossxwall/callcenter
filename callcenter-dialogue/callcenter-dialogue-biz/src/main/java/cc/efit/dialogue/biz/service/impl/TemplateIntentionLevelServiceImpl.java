package cc.efit.dialogue.biz.service.impl;

import cc.efit.dialogue.api.enums.TemplateIntentionLevelTypeEnum;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import cc.efit.dialogue.biz.vo.intention.IntentionLevelInfo;
import cc.efit.dialogue.biz.vo.intention.IntentionLevelSetSortInfo;
import cc.efit.enums.CommonOperatorEnum;
import cc.efit.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateIntentionLevelMapper;
import cc.efit.dialogue.biz.repository.TemplateIntentionLevelRepository;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionLevelDto;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionLevelQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateIntentionLevel;
import cc.efit.dialogue.biz.service.TemplateIntentionLevelService;
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
 * 意向等级Service业务层处理
 * 
 * @author across
 * @date 2025-08-13
 */
@Service
@RequiredArgsConstructor
public class TemplateIntentionLevelServiceImpl implements TemplateIntentionLevelService  {

    private final TemplateIntentionLevelRepository templateIntentionLevelRepository;
    private final TemplateIntentionLevelMapper templateIntentionLevelMapper;
    private final CallTemplateOperatorService callTemplateOperatorService;

    @Override
    public PageResult<TemplateIntentionLevelDto> queryAll(TemplateIntentionLevelQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.ASC,"sort");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateIntentionLevel> page = templateIntentionLevelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateIntentionLevelMapper::toDto));
    }

    @Override
    public List<TemplateIntentionLevelDto> queryAll(TemplateIntentionLevelQueryCriteria criteria){
        return templateIntentionLevelMapper.toDto(templateIntentionLevelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询意向等级
     * 
     * @param id 意向等级主键
     * @return 意向等级
     */
    @Override
    public TemplateIntentionLevelDto selectTemplateIntentionLevelById(Integer id)  {
        return templateIntentionLevelMapper.toDto(templateIntentionLevelRepository.findById(id).orElseGet(TemplateIntentionLevel::new));
    }


    /**
     * 新增意向等级
     * 
     * @param templateIntentionLevel 意向等级
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateIntentionLevel(TemplateIntentionLevel templateIntentionLevel) {
        //全部都是非默认意向等级
        validTemplateIntentionLevelName(templateIntentionLevel);
        templateIntentionLevel.setType(TemplateIntentionLevelTypeEnum.NORMAL.getType());
        int sort = 1;
        sort += templateIntentionLevelRepository.countByCallTemplateId(templateIntentionLevel.getCallTemplateId());
        templateIntentionLevel.setSort(sort);
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateIntentionLevel.getCallTemplateId(), CommonOperatorEnum.ADD.getCode(), "新增意向等级"+templateIntentionLevel.getName());
        templateIntentionLevelRepository.save(templateIntentionLevel);
    }

    /**
     * 修改意向等级
     * 
     * @param templateIntentionLevel 意向等级
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateIntentionLevel(TemplateIntentionLevel templateIntentionLevel) {
        validTemplateIntentionLevelName(templateIntentionLevel);
        TemplateIntentionLevel level = templateIntentionLevelRepository.findById(templateIntentionLevel.getId()).orElse(null);
        if (level==null) {
            throw new BadRequestException( "意向等级不存在");
        }
        level.setSort(templateIntentionLevel.getSort());
        level.setName(templateIntentionLevel.getName());
        level.setDescription(templateIntentionLevel.getDescription());
        level.setRuleContent(templateIntentionLevel.getRuleContent());
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateIntentionLevel.getCallTemplateId(), CommonOperatorEnum.UPDATE.getCode(), "修改意向等级"+ templateIntentionLevel.getName());
        templateIntentionLevelRepository.save(templateIntentionLevel);
    }

    /**
     * 批量删除意向等级
     * 
     * @param ids 需要删除的意向等级主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateIntentionLevelByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateIntentionLevelById(id);
        }
    }

    /**
     * 删除意向等级信息
     * 
     * @param id 意向等级主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateIntentionLevelById(Integer id) {
        TemplateIntentionLevel level = templateIntentionLevelRepository.findById(id).orElse(null);
        if (level==null) {
            return;
        }
        callTemplateOperatorService.buildCallTemplateOperatorLog(level.getCallTemplateId(), CommonOperatorEnum.DELETE.getCode(), "删除意向等级" + level.getName());
        templateIntentionLevelRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateIntentionLevelDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateIntentionLevelDto templateIntentionLevel : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateIntentionLevel.getId());
            map.put("callTemplateId",  templateIntentionLevel.getCallTemplateId());
            map.put("name",  templateIntentionLevel.getName());
            map.put("description",  templateIntentionLevel.getDescription());
            map.put("type",  templateIntentionLevel.getType());
            map.put("sort",  templateIntentionLevel.getSort());
            map.put("ruleContent",  templateIntentionLevel.getRuleContent());
            map.put("createBy",  templateIntentionLevel.getCreateBy());
            map.put("updateBy",  templateIntentionLevel.getUpdateBy());
            map.put("createTime",  templateIntentionLevel.getCreateTime());
            map.put("deleted",  templateIntentionLevel.getDeleted());
            map.put("updateTime",  templateIntentionLevel.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<IntentionLevelInfo> findTemplateIntentionLevelListByCallTemplateId(Integer callTemplateId) {
        List<TemplateIntentionLevel> list= templateIntentionLevelRepository.findByCallTemplateIdOrderBySortAsc(callTemplateId);
        return list==null?null:list.stream().map(i-> new IntentionLevelInfo(i.getName(),i.getDescription(),i.getType())).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTemplateDefaultIntentionByCallTemplateId(Integer callTemplateId, String level) {
        templateIntentionLevelRepository.updateTemplateCommonLevel(callTemplateId);
        templateIntentionLevelRepository.updateTemplateDefaultLevel(callTemplateId,level);
        callTemplateOperatorService.buildCallTemplateOperatorLog(callTemplateId, CommonOperatorEnum.UPDATE.getCode(), "设置默认意向等级为"+level);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTemplateIntentionLevelSort(IntentionLevelSetSortInfo info) {
        TemplateIntentionLevel level = templateIntentionLevelRepository.findById(info.id()).orElseThrow(()->new BadRequestException("意向等级不存在"));
        int sourceSort = level.getSort();
        int targetSort = info.sort() + 1;
        if (sourceSort == targetSort) {
            return;
        }
        if (sourceSort < targetSort) {
            //下移
            templateIntentionLevelRepository.updateTemplateIntentionLevelSortDown(sourceSort, targetSort, info.callTemplateId());
        }else{
            //上移
            templateIntentionLevelRepository.updateTemplateIntentionLevelSortUp(sourceSort, targetSort, info.callTemplateId());
        }
        level.setSort(targetSort);
        templateIntentionLevelRepository.save(level);

    }


    private void validTemplateIntentionLevelName( TemplateIntentionLevel intentionLevel ) {
        String name = intentionLevel.getName();
        TemplateIntentionLevel level = templateIntentionLevelRepository.findByNameAndCallTemplateId(name,intentionLevel.getCallTemplateId());
        if (level==null) {
            return ;
        }
        if (intentionLevel.getId()==null || !level.getId().equals(intentionLevel.getId())) {
            throw new BadRequestException("意向等级重复");
        }
    }
}
