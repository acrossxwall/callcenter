package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateGlobalTtsMapper;
import cc.efit.dialogue.biz.repository.TemplateGlobalTtsRepository;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateGlobalTts;
import cc.efit.dialogue.biz.service.TemplateGlobalTtsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import java.util.List;

/**
 * tts全局设置Service业务层处理
 * 
 * @author across
 * @date 2025-08-21
 */
@Service
@RequiredArgsConstructor
public class TemplateGlobalTtsServiceImpl implements TemplateGlobalTtsService  {

    private final TemplateGlobalTtsRepository templateGlobalTtsRepository;
    private final TemplateGlobalTtsMapper templateGlobalTtsMapper;

    @Override
    public PageResult<TemplateGlobalTtsDto> queryAll(TemplateGlobalTtsQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateGlobalTts> page = templateGlobalTtsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateGlobalTtsMapper::toDto));
    }

    @Override
    public List<TemplateGlobalTtsDto> queryAll(TemplateGlobalTtsQueryCriteria criteria){
        return templateGlobalTtsMapper.toDto(templateGlobalTtsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询tts全局设置
     * 
     * @param id tts全局设置主键
     * @return tts全局设置
     */
    @Override
    public TemplateGlobalTtsDto selectTemplateGlobalTtsById(Integer id)  {
        return templateGlobalTtsMapper.toDto(templateGlobalTtsRepository.findById(id).orElseGet(TemplateGlobalTts::new));
    }


    /**
     * 新增tts全局设置
     * 
     * @param templateGlobalTts tts全局设置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateGlobalTts(TemplateGlobalTts templateGlobalTts) {
        templateGlobalTtsRepository.save(templateGlobalTts);
    }

    /**
     * 修改tts全局设置
     * 
     * @param templateGlobalTts tts全局设置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateGlobalTts(TemplateGlobalTts templateGlobalTts) {
        templateGlobalTtsRepository.save(templateGlobalTts);
    }

    /**
     * 批量删除tts全局设置
     * 
     * @param ids 需要删除的tts全局设置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalTtsByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateGlobalTtsById(id);
        }
    }

    /**
     * 删除tts全局设置信息
     * 
     * @param id tts全局设置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalTtsById(Integer id) {
        templateGlobalTtsRepository.logicDeleteById(id);
    }

    @Override
    public TemplateGlobalTts findTemplateGlobalTtsByCallTemplateId(Integer callTemplateId) {
        return templateGlobalTtsRepository.findByCallTemplateId(callTemplateId);
    }
}
