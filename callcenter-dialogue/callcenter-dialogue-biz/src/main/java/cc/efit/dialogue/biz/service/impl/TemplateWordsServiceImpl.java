package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateWordsMapper;
import cc.efit.dialogue.biz.repository.TemplateWordsRepository;
import cc.efit.dialogue.biz.service.dto.TemplateWordsDto;
import cc.efit.dialogue.biz.service.dto.TemplateWordsQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateWords;
import cc.efit.dialogue.biz.service.TemplateWordsService;
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
 * 关键词库Service业务层处理
 * 
 * @author across
 * @date 2025-08-19
 */
@Service
@RequiredArgsConstructor
public class TemplateWordsServiceImpl implements TemplateWordsService  {

    private final TemplateWordsRepository templateWordsRepository;
    private final TemplateWordsMapper templateWordsMapper;

    @Override
    public PageResult<TemplateWordsDto> queryAll(TemplateWordsQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateWords> page = templateWordsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateWordsMapper::toDto));
    }

    @Override
    public List<TemplateWordsDto> queryAll(TemplateWordsQueryCriteria criteria){
        return templateWordsMapper.toDto(templateWordsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询关键词库
     * 
     * @param id 关键词库主键
     * @return 关键词库
     */
    @Override
    public TemplateWordsDto selectTemplateWordsById(Integer id)  {
        return templateWordsMapper.toDto(templateWordsRepository.findById(id).orElseGet(TemplateWords::new));
    }


    /**
     * 新增关键词库
     * 
     * @param templateWords 关键词库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateWords(TemplateWords templateWords) {
        templateWordsRepository.save(templateWords);
    }

    /**
     * 修改关键词库
     * 
     * @param templateWords 关键词库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateWords(TemplateWords templateWords) {
        templateWordsRepository.save(templateWords);
    }

    /**
     * 批量删除关键词库
     * 
     * @param ids 需要删除的关键词库主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateWordsByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateWordsById(id);
        }
    }

    /**
     * 删除关键词库信息
     * 
     * @param id 关键词库主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateWordsById(Integer id) {
        templateWordsRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateWordsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateWordsDto templateWords : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateWords.getId());
            map.put("deptId",  templateWords.getDeptId());
            map.put("userId",  templateWords.getUserId());
            map.put("name",  templateWords.getName());
            map.put("categoryId",  templateWords.getCategoryId());
            map.put("createBy",  templateWords.getCreateBy());
            map.put("updateBy",  templateWords.getUpdateBy());
            map.put("createTime",  templateWords.getCreateTime());
            map.put("deleted",  templateWords.getDeleted());
            map.put("updateTime",  templateWords.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<String> selectAllByCategoryIds(List<Integer> categoryIds) {
        List<TemplateWords> all = templateWordsRepository.findByCategoryIdIn (categoryIds );
        return all==null?null:all.stream().map(TemplateWords::getName).toList();
    }

}
