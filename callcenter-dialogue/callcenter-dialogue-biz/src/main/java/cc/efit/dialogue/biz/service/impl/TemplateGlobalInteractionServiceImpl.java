package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateGlobalInteractionMapper;
import cc.efit.dialogue.biz.repository.TemplateGlobalInteractionRepository;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalInteractionDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalInteractionQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateGlobalInteraction;
import cc.efit.dialogue.biz.service.TemplateGlobalInteractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import java.util.List;

/**
 * 交互全局设置Service业务层处理
 * 
 * @author across
 * @date 2025-08-21
 */
@Service
@RequiredArgsConstructor
public class TemplateGlobalInteractionServiceImpl implements TemplateGlobalInteractionService  {

    private final TemplateGlobalInteractionRepository templateGlobalInteractionRepository;
    private final TemplateGlobalInteractionMapper templateGlobalInteractionMapper;

    @Override
    public PageResult<TemplateGlobalInteractionDto> queryAll(TemplateGlobalInteractionQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateGlobalInteraction> page = templateGlobalInteractionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateGlobalInteractionMapper::toDto));
    }

    @Override
    public List<TemplateGlobalInteractionDto> queryAll(TemplateGlobalInteractionQueryCriteria criteria){
        return templateGlobalInteractionMapper.toDto(templateGlobalInteractionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询交互全局设置
     * 
     * @param id 交互全局设置主键
     * @return 交互全局设置
     */
    @Override
    public TemplateGlobalInteractionDto selectTemplateGlobalInteractionById(Integer id)  {
        return templateGlobalInteractionMapper.toDto(templateGlobalInteractionRepository.findById(id).orElseGet(TemplateGlobalInteraction::new));
    }


    /**
     * 新增交互全局设置
     * 
     * @param templateGlobalInteraction 交互全局设置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateGlobalInteraction(TemplateGlobalInteraction templateGlobalInteraction) {
        templateGlobalInteractionRepository.save(templateGlobalInteraction);
    }

    /**
     * 修改交互全局设置
     * 
     * @param templateGlobalInteraction 交互全局设置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateGlobalInteraction(TemplateGlobalInteraction templateGlobalInteraction) {
        templateGlobalInteractionRepository.save(templateGlobalInteraction);
    }

    /**
     * 批量删除交互全局设置
     * 
     * @param ids 需要删除的交互全局设置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalInteractionByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateGlobalInteractionById(id);
        }
    }

    /**
     * 删除交互全局设置信息
     * 
     * @param id 交互全局设置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalInteractionById(Integer id) {
        templateGlobalInteractionRepository.logicDeleteById(id);
    }

    @Override
    public TemplateGlobalInteraction findTemplateGlobalInteractionByCallTemplateId(Integer callTemplateId) {
        return templateGlobalInteractionRepository.findByCallTemplateId(callTemplateId);
    }
}
