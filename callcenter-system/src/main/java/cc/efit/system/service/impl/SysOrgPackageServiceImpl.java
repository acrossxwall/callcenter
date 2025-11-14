package cc.efit.system.service.impl;

import cc.efit.enums.CommonStatusEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.service.RoleService;
import cc.efit.system.domain.SysOrg;
import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.system.repository.SysOrgPackageMenusRepository;
import cc.efit.system.repository.SysOrgRepository;
import cc.efit.system.req.SysOrgPackageReq;
import cc.efit.system.res.OrgPackageInfo;
import cc.efit.system.res.SysOrgPackageRes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.system.service.mapstruct.SysOrgPackageMapper;
import cc.efit.system.repository.SysOrgPackageRepository;
import cc.efit.system.service.dto.SysOrgPackageDto;
import cc.efit.system.service.dto.SysOrgPackageQueryCriteria;
import cc.efit.system.domain.SysOrgPackage;
import cc.efit.system.service.SysOrgPackageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.util.*;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * 租户套餐表Service业务层处理
 * 
 * @author across
 * @date 2025-10-28
 */
@Service
@RequiredArgsConstructor
public class SysOrgPackageServiceImpl implements SysOrgPackageService  {

    private final SysOrgPackageRepository sysOrgPackageRepository;
    private final SysOrgPackageMenusRepository sysOrgPackageMenusRepository;
    private final SysOrgPackageMapper sysOrgPackageMapper;
    private final RoleService roleService;
    private final SysOrgRepository sysOrgRepository;

    @Override
    public PageResult<SysOrgPackageDto> queryAll(SysOrgPackageQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<SysOrgPackage> page = sysOrgPackageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(sysOrgPackageMapper::toDto));
    }

    @Override
    public List<SysOrgPackageDto> queryAll(SysOrgPackageQueryCriteria criteria){
        return sysOrgPackageMapper.toDto(sysOrgPackageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询租户套餐表
     * 
     * @param id 租户套餐表主键
     * @return 租户套餐表
     */
    @Override
    public SysOrgPackageRes selectSysOrgPackageById(Integer id)  {
        SysOrgPackage orgPackage = sysOrgPackageRepository.findById(id).orElseGet(SysOrgPackage::new);
        if (orgPackage.getId()==null) {
            return null;
        }
        Set<Integer> menuIds = buildPackageMenuIds(orgPackage.getId());
        return new SysOrgPackageRes(orgPackage.getId(), orgPackage.getName(), orgPackage.getStatus(),
                orgPackage.getRemark(), menuIds, orgPackage.getCreateTime());
    }


    /**
     * 新增租户套餐表
     * 
     * @param req 租户套餐表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSysOrgPackage(SysOrgPackageReq req) {
        validateOrgPackage(req);
        SysOrgPackage sysOrgPackage = new SysOrgPackage();
        sysOrgPackage.setName(req.name());
        sysOrgPackage.setRemark(req.remark());
        sysOrgPackage.setStatus(req.status());
        sysOrgPackageRepository.save(sysOrgPackage);
        Integer packageId = sysOrgPackage.getId();
        buildPackageMenus(req.menuIds(), packageId);
    }

    /**
     * 修改租户套餐表
     * 
     * @param req 租户套餐表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysOrgPackage(SysOrgPackageReq req) {
        validateOrgPackage(req);
        SysOrgPackage sysOrgPackage = sysOrgPackageRepository.findById(req.id()).orElseThrow(() -> new BadRequestException("租户套餐不存在"));
        sysOrgPackage.setName(req.name());
        sysOrgPackage.setRemark(req.remark());
        sysOrgPackage.setStatus(req.status());
        sysOrgPackageRepository.save(sysOrgPackage);
        //清理旧的角色缓存
        //查询旧的的集合menu ids
        Set<Integer> oldMenuIds = buildPackageMenuIds(sysOrgPackage.getId());
        List<Integer> menuIds = req.menuIds();
        Set<Integer> delete = oldMenuIds.stream().filter(id -> !menuIds.contains(id)).collect(Collectors.toSet());
        Set<Integer> add = menuIds.stream().filter(id -> !oldMenuIds.contains(id)).collect(Collectors.toSet());
        if (!delete.isEmpty()) {
            //删除旧的
            sysOrgPackageMenusRepository.deleteByPackageIdAndMenuIdIn(sysOrgPackage.getId(), delete);
        }
        if (!add.isEmpty()) {
            //新增的
            buildPackageMenus(add, sysOrgPackage.getId());
        }
        List<SysOrg> list = sysOrgRepository.findByPackageIdAndStatus(sysOrgPackage.getId(), CommonStatusEnum.ENABLE.getCode());
        if (list==null || list.isEmpty()) {
            //该套餐下没有机构再使用 do nothing
            return;
        }
        //清理机构对应的信息
        roleService.clearOrgPackageCache(add, delete, list.stream().map(SysOrg::getId).collect(Collectors.toSet()));
    }
    @Override
    public Set<Integer> buildPackageMenuIds(Integer id) {
        List<SysOrgPackageMenus> list = sysOrgPackageMenusRepository.findByPackageId(id);
        if (list==null || list.isEmpty()) {
            return new HashSet<>();
        }
        return list.stream().map(SysOrgPackageMenus::getMenuId).collect(Collectors.toSet());
    }

    @Override
    public List<OrgPackageInfo> getEnablePackageList() {
        List<SysOrgPackage> list = sysOrgPackageRepository.findByStatus(CommonStatusEnum.ENABLE.getCode());
        if (list==null || list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(s->new OrgPackageInfo(s.getId(),s.getName())).collect(Collectors.toList());
    }

    private void validateOrgPackage(SysOrgPackageReq req) {
        if (StringUtils.isBlank(req.name())) {
            throw new BadRequestException("套餐名称不能为空");
        }
        if (CollectionUtils.isEmpty(req.menuIds())) {
            throw new BadRequestException("菜单不能为空");
        }
        SysOrgPackage exists = sysOrgPackageRepository.findByName(req.name());
        if (exists==null ) {
            return;
        }
        if (exists.getId()==null || !exists.getId().equals(req.id())) {
            throw new BadRequestException("套餐名称已存在");
        }
    }

    private void buildPackageMenus(Collection<Integer> menuIds, Integer packageId) {
        List<SysOrgPackageMenus> menus = menuIds.stream().map(menuId -> {
            SysOrgPackageMenus sysOrgPackageMenus = new SysOrgPackageMenus();
            sysOrgPackageMenus.setMenuId(menuId);
            sysOrgPackageMenus.setPackageId(packageId);
            return sysOrgPackageMenus;
        }).toList();
        sysOrgPackageMenusRepository.saveAll(menus);
    }


    /**
     * 批量删除租户套餐表
     * 
     * @param ids 需要删除的租户套餐表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysOrgPackageByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteSysOrgPackageById(id);
        }
    }

    /**
     * 删除租户套餐表信息
     * 
     * @param id 租户套餐表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysOrgPackageById(Integer id) {
        sysOrgPackageRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<SysOrgPackageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysOrgPackageDto sysOrgPackage : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  sysOrgPackage.getId());
            map.put("name",  sysOrgPackage.getName());
            map.put("status",  sysOrgPackage.getStatus());
            map.put("remark",  sysOrgPackage.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
