package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateGlobalDefaultVerbalMapper;
import cc.efit.dialogue.biz.repository.TemplateGlobalDefaultVerbalRepository;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalDefaultVerbalDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalDefaultVerbalQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateGlobalDefaultVerbal;
import cc.efit.dialogue.biz.service.TemplateGlobalDefaultVerbalService;
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
 * 兜底话术全局设置Service业务层处理
 * 
 * @author across
 * @date 2025-11-11
 */
@Service
@RequiredArgsConstructor
public class TemplateGlobalDefaultVerbalServiceImpl implements TemplateGlobalDefaultVerbalService  {

    private final TemplateGlobalDefaultVerbalRepository templateGlobalDefaultVerbalRepository;
    private final TemplateGlobalDefaultVerbalMapper templateGlobalDefaultVerbalMapper;

    @Override
    public PageResult<TemplateGlobalDefaultVerbalDto> queryAll(TemplateGlobalDefaultVerbalQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateGlobalDefaultVerbal> page = templateGlobalDefaultVerbalRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateGlobalDefaultVerbalMapper::toDto));
    }

    @Override
    public List<TemplateGlobalDefaultVerbalDto> queryAll(TemplateGlobalDefaultVerbalQueryCriteria criteria){
        return templateGlobalDefaultVerbalMapper.toDto(templateGlobalDefaultVerbalRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询兜底话术全局设置
     * 
     * @param id 兜底话术全局设置主键
     * @return 兜底话术全局设置
     */
    @Override
    public TemplateGlobalDefaultVerbalDto selectTemplateGlobalDefaultVerbalById(Integer id)  {
        return templateGlobalDefaultVerbalMapper.toDto(templateGlobalDefaultVerbalRepository.findById(id).orElseGet(TemplateGlobalDefaultVerbal::new));
    }


    /**
     * 新增兜底话术全局设置
     * 
     * @param templateGlobalDefaultVerbal 兜底话术全局设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateGlobalDefaultVerbal(TemplateGlobalDefaultVerbal templateGlobalDefaultVerbal) {
        templateGlobalDefaultVerbalRepository.save(templateGlobalDefaultVerbal);
    }

    /**
     * 修改兜底话术全局设置
     * 
     * @param templateGlobalDefaultVerbal 兜底话术全局设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateGlobalDefaultVerbal(TemplateGlobalDefaultVerbal templateGlobalDefaultVerbal) {
        templateGlobalDefaultVerbalRepository.save(templateGlobalDefaultVerbal);
    }

    /**
     * 批量删除兜底话术全局设置
     * 
     * @param ids 需要删除的兜底话术全局设置主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalDefaultVerbalByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateGlobalDefaultVerbalById(id);
        }
    }

    /**
     * 删除兜底话术全局设置信息
     * 
     * @param id 兜底话术全局设置主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalDefaultVerbalById(Integer id) {
        templateGlobalDefaultVerbalRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateGlobalDefaultVerbalDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateGlobalDefaultVerbalDto templateGlobalDefaultVerbal : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateGlobalDefaultVerbal.getId());
            map.put("callTemplateId",  templateGlobalDefaultVerbal.getCallTemplateId());
            map.put("enableDefault",  templateGlobalDefaultVerbal.getEnableDefault());
            map.put("defaultAction",  templateGlobalDefaultVerbal.getDefaultAction());
            map.put("targetFlowId",  templateGlobalDefaultVerbal.getTargetFlowId());
            map.put("verbalId",  templateGlobalDefaultVerbal.getVerbalId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TemplateGlobalDefaultVerbal findTemplateGlobalDefaultVerbalByCallTemplateId(Integer callTemplateId) {
        return templateGlobalDefaultVerbalRepository.findByCallTemplateId(callTemplateId);
    }

}
