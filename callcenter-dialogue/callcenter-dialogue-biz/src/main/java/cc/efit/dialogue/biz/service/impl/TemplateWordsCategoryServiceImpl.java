package cc.efit.dialogue.biz.service.impl;

import cc.efit.dialogue.biz.vo.words.CategoryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateWordsCategoryMapper;
import cc.efit.dialogue.biz.repository.TemplateWordsCategoryRepository;
import cc.efit.dialogue.biz.service.dto.TemplateWordsCategoryDto;
import cc.efit.dialogue.biz.service.dto.TemplateWordsCategoryQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateWordsCategory;
import cc.efit.dialogue.biz.service.TemplateWordsCategoryService;
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
 * 词库分类Service业务层处理
 * 
 * @author across
 * @date 2025-08-19
 */
@Service
@RequiredArgsConstructor
public class TemplateWordsCategoryServiceImpl implements TemplateWordsCategoryService  {

    private final TemplateWordsCategoryRepository templateWordsCategoryRepository;
    private final TemplateWordsCategoryMapper templateWordsCategoryMapper;

    @Override
    public PageResult<TemplateWordsCategoryDto> queryAll(TemplateWordsCategoryQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateWordsCategory> page = templateWordsCategoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateWordsCategoryMapper::toDto));
    }

    @Override
    public List<TemplateWordsCategoryDto> queryAll(TemplateWordsCategoryQueryCriteria criteria){
        return templateWordsCategoryMapper.toDto(templateWordsCategoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询词库分类
     * 
     * @param id 词库分类主键
     * @return 词库分类
     */
    @Override
    public TemplateWordsCategoryDto selectTemplateWordsCategoryById(Integer id)  {
        return templateWordsCategoryMapper.toDto(templateWordsCategoryRepository.findById(id).orElseGet(TemplateWordsCategory::new));
    }


    /**
     * 新增词库分类
     * 
     * @param templateWordsCategory 词库分类
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateWordsCategory(TemplateWordsCategory templateWordsCategory) {
        templateWordsCategoryRepository.save(templateWordsCategory);
    }

    /**
     * 修改词库分类
     * 
     * @param templateWordsCategory 词库分类
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateWordsCategory(TemplateWordsCategory templateWordsCategory) {
        templateWordsCategoryRepository.save(templateWordsCategory);
    }

    /**
     * 批量删除词库分类
     * 
     * @param ids 需要删除的词库分类主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateWordsCategoryByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateWordsCategoryById(id);
        }
    }

    /**
     * 删除词库分类信息
     * 
     * @param id 词库分类主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateWordsCategoryById(Integer id) {
        templateWordsCategoryRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateWordsCategoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateWordsCategoryDto templateWordsCategory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateWordsCategory.getId());
            map.put("deptId",  templateWordsCategory.getDeptId());
            map.put("userId",  templateWordsCategory.getUserId());
            map.put("name",  templateWordsCategory.getName());
            map.put("type",  templateWordsCategory.getType());
            map.put("createBy",  templateWordsCategory.getCreateBy());
            map.put("updateBy",  templateWordsCategory.getUpdateBy());
            map.put("createTime",  templateWordsCategory.getCreateTime());
            map.put("deleted",  templateWordsCategory.getDeleted());
            map.put("updateTime",  templateWordsCategory.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<CategoryInfo> getAllCategoryInfo() {
        List<TemplateWordsCategory> list = templateWordsCategoryRepository.findAll();
        return list.stream().map(s->new CategoryInfo(s.getId(),s.getName())).toList();
    }

}
