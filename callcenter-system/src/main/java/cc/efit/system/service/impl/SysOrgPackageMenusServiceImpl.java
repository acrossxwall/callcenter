package cc.efit.system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.system.service.mapstruct.SysOrgPackageMenusMapper;
import cc.efit.system.repository.SysOrgPackageMenusRepository;
import cc.efit.system.service.dto.SysOrgPackageMenusDto;
import cc.efit.system.service.dto.SysOrgPackageMenusQueryCriteria;
import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.system.service.SysOrgPackageMenusService;
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
 * 租户套餐菜单关联表Service业务层处理
 * 
 * @author across
 * @date 2025-10-28
 */
@Service
@RequiredArgsConstructor
public class SysOrgPackageMenusServiceImpl implements SysOrgPackageMenusService  {

    private final SysOrgPackageMenusRepository sysOrgPackageMenusRepository;
    private final SysOrgPackageMenusMapper sysOrgPackageMenusMapper;

    @Override
    public PageResult<SysOrgPackageMenusDto> queryAll(SysOrgPackageMenusQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<SysOrgPackageMenus> page = sysOrgPackageMenusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(sysOrgPackageMenusMapper::toDto));
    }

    @Override
    public List<SysOrgPackageMenusDto> queryAll(SysOrgPackageMenusQueryCriteria criteria){
        return sysOrgPackageMenusMapper.toDto(sysOrgPackageMenusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询租户套餐菜单关联表
     * 
     * @param id 租户套餐菜单关联表主键
     * @return 租户套餐菜单关联表
     */
    @Override
    public SysOrgPackageMenusDto selectSysOrgPackageMenusById(Integer id)  {
        return sysOrgPackageMenusMapper.toDto(sysOrgPackageMenusRepository.findById(id).orElseGet(SysOrgPackageMenus::new));
    }


    /**
     * 新增租户套餐菜单关联表
     * 
     * @param sysOrgPackageMenus 租户套餐菜单关联表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSysOrgPackageMenus(SysOrgPackageMenus sysOrgPackageMenus) {
        sysOrgPackageMenusRepository.save(sysOrgPackageMenus);
    }

    /**
     * 修改租户套餐菜单关联表
     * 
     * @param sysOrgPackageMenus 租户套餐菜单关联表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysOrgPackageMenus(SysOrgPackageMenus sysOrgPackageMenus) {
        sysOrgPackageMenusRepository.save(sysOrgPackageMenus);
    }

    /**
     * 批量删除租户套餐菜单关联表
     * 
     * @param ids 需要删除的租户套餐菜单关联表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysOrgPackageMenusByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteSysOrgPackageMenusById(id);
        }
    }

    /**
     * 删除租户套餐菜单关联表信息
     * 
     * @param id 租户套餐菜单关联表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysOrgPackageMenusById(Integer id) {
        sysOrgPackageMenusRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<SysOrgPackageMenusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysOrgPackageMenusDto sysOrgPackageMenus : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  sysOrgPackageMenus.getId());
            map.put("menuId",  sysOrgPackageMenus.getMenuId());
            map.put("packageId",  sysOrgPackageMenus.getPackageId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
