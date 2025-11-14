package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateGlobalNluMapper;
import cc.efit.dialogue.biz.repository.TemplateGlobalNluRepository;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNluDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNluQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateGlobalNlu;
import cc.efit.dialogue.biz.service.TemplateGlobalNluService;
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
 * nlu全局设置Service业务层处理
 * 
 * @author across
 * @date 2025-11-10
 */
@Service
@RequiredArgsConstructor
public class TemplateGlobalNluServiceImpl implements TemplateGlobalNluService  {

    private final TemplateGlobalNluRepository templateGlobalNluRepository;
    private final TemplateGlobalNluMapper templateGlobalNluMapper;

    @Override
    public PageResult<TemplateGlobalNluDto> queryAll(TemplateGlobalNluQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateGlobalNlu> page = templateGlobalNluRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateGlobalNluMapper::toDto));
    }

    @Override
    public List<TemplateGlobalNluDto> queryAll(TemplateGlobalNluQueryCriteria criteria){
        return templateGlobalNluMapper.toDto(templateGlobalNluRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询nlu全局设置
     * 
     * @param id nlu全局设置主键
     * @return nlu全局设置
     */
    @Override
    public TemplateGlobalNluDto selectTemplateGlobalNluById(Integer id)  {
        return templateGlobalNluMapper.toDto(templateGlobalNluRepository.findById(id).orElseGet(TemplateGlobalNlu::new));
    }


    /**
     * 新增nlu全局设置
     * 
     * @param templateGlobalNlu nlu全局设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateGlobalNlu(TemplateGlobalNlu templateGlobalNlu) {
        templateGlobalNluRepository.save(templateGlobalNlu);
    }

    /**
     * 修改nlu全局设置
     * 
     * @param templateGlobalNlu nlu全局设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateGlobalNlu(TemplateGlobalNlu templateGlobalNlu) {
        templateGlobalNluRepository.save(templateGlobalNlu);
    }

    /**
     * 批量删除nlu全局设置
     * 
     * @param ids 需要删除的nlu全局设置主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalNluByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateGlobalNluById(id);
        }
    }

    /**
     * 删除nlu全局设置信息
     * 
     * @param id nlu全局设置主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateGlobalNluById(Integer id) {
        templateGlobalNluRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateGlobalNluDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateGlobalNluDto templateGlobalNlu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateGlobalNlu.getId());
            map.put("callTemplateId",  templateGlobalNlu.getCallTemplateId());
            map.put("enableNlu",  templateGlobalNlu.getEnableNlu());
            map.put("modeId",  templateGlobalNlu.getModeId());
            map.put("threshold",  templateGlobalNlu.getThreshold());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TemplateGlobalNlu findTemplateGlobalNluByCallTemplateId(Integer callTemplateId) {
        return templateGlobalNluRepository.findByCallTemplateId(callTemplateId);
    }

}
