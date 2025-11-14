package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateGlobalNoreplyMapper;
import cc.efit.dialogue.biz.repository.TemplateGlobalNoreplyRepository;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNoreplyDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNoreplyQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateGlobalNoreply;
import cc.efit.dialogue.biz.service.TemplateGlobalNoreplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import java.util.List;

/**
 * 无应答全局设置Service业务层处理
 * 
 * @author across
 * @date 2025-08-21
 */
@Service
@RequiredArgsConstructor
public class TemplateGlobalNoreplyServiceImpl implements TemplateGlobalNoreplyService  {

    private final TemplateGlobalNoreplyRepository templateGlobalNoreplyRepository;
    private final TemplateGlobalNoreplyMapper templateGlobalNoreplyMapper;

    @Override
    public PageResult<TemplateGlobalNoreplyDto> queryAll(TemplateGlobalNoreplyQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateGlobalNoreply> page = templateGlobalNoreplyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateGlobalNoreplyMapper::toDto));
    }

    @Override
    public List<TemplateGlobalNoreplyDto> queryAll(TemplateGlobalNoreplyQueryCriteria criteria){
        return templateGlobalNoreplyMapper.toDto(templateGlobalNoreplyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询无应答全局设置
     * 
     * @param id 无应答全局设置主键
     * @return 无应答全局设置
     */
    @Override
    public TemplateGlobalNoreplyDto selectTemplateGlobalNoreplyById(Integer id)  {
        return templateGlobalNoreplyMapper.toDto(templateGlobalNoreplyRepository.findById(id).orElseGet(TemplateGlobalNoreply::new));
    }


    /**
     * 新增无应答全局设置
     * 
     * @param templateGlobalNoreply 无应答全局设置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateGlobalNoreply(TemplateGlobalNoreply templateGlobalNoreply) {
        templateGlobalNoreplyRepository.save(templateGlobalNoreply);
    }

    /**
     * 修改无应答全局设置
     * 
     * @param templateGlobalNoreply 无应答全局设置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateGlobalNoreply(TemplateGlobalNoreply templateGlobalNoreply) {
        templateGlobalNoreplyRepository.save(templateGlobalNoreply);
    }

    /**
     * 批量删除无应答全局设置
     * 
     * @param ids 需要删除的无应答全局设置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalNoreplyByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateGlobalNoreplyById(id);
        }
    }

    /**
     * 删除无应答全局设置信息
     * 
     * @param id 无应答全局设置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalNoreplyById(Integer id) {
        templateGlobalNoreplyRepository.logicDeleteById(id);
    }

    @Override
    public TemplateGlobalNoreply findTemplateGlobalNoreplyByCallTemplateId(Integer callTemplateId) {
        return templateGlobalNoreplyRepository.findByCallTemplateId(callTemplateId);
    }

}
