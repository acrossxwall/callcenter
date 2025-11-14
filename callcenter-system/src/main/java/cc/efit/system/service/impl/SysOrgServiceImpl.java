package cc.efit.system.service.impl;

import cc.efit.enums.CommonStatusEnum;
import cc.efit.enums.DataScopeEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.domain.Role;
import cc.efit.modules.system.repository.RoleRepository;
import cc.efit.modules.system.service.RoleService;
import cc.efit.modules.system.service.UserService;
import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.system.repository.SysOrgPackageMenusRepository;
import cc.efit.system.service.SysOrgPackageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.system.service.mapstruct.SysOrgMapper;
import cc.efit.system.repository.SysOrgRepository;
import cc.efit.system.service.dto.SysOrgDto;
import cc.efit.system.service.dto.SysOrgQueryCriteria;
import cc.efit.system.domain.SysOrg;
import cc.efit.system.service.SysOrgService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 多租户Service业务层处理
 * 
 * @author across
 * @date 2025-08-06
 */
@Service
@RequiredArgsConstructor
public class SysOrgServiceImpl implements SysOrgService  {

    private final SysOrgRepository sysOrgRepository;
    private final SysOrgMapper sysOrgMapper;
    private final SysOrgPackageMenusRepository sysOrgPackageMenusRepository;
    private final RoleRepository roleRepository;
    private final SysOrgPackageService sysOrgPackageService;
    private final RoleService roleService;
    private final UserService userService;

    @Override
    public PageResult<SysOrgDto> queryAll(SysOrgQueryCriteria criteria, Pageable pageable){
        Page<SysOrg> page = sysOrgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(sysOrgMapper::toDto));
    }

    @Override
    public List<SysOrgDto> queryAll(SysOrgQueryCriteria criteria){
        return sysOrgMapper.toDto(sysOrgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询多租户
     * 
     * @param id 多租户主键
     * @return 多租户
     */
    @Override
    public SysOrgDto selectSysOrgById(Integer id)  {
        return sysOrgMapper.toDto(sysOrgRepository.findById(id).orElseGet(SysOrg::new));
    }


    /**
     * 新增多租户
     * 
     * @param sysOrg 多租户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSysOrg(SysOrg sysOrg) {
        validSysOrg(sysOrg);
        sysOrgRepository.save(sysOrg);
        buildSysOrgAdmin(sysOrg.getId(), sysOrg.getPackageId());
    }


    /**
     * 修改多租户
     * 
     * @param sysOrg 多租户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysOrg(SysOrg sysOrg) {
        validSysOrg(sysOrg);
        SysOrg org = sysOrgRepository.findById(sysOrg.getId()).orElseThrow(()->new BadRequestException("租户不存在"));
        if (!sysOrg.getPackageId().equals(org.getPackageId())) {
            //套餐变了
            Integer roleId = roleRepository.findOrgAdminRoleByOrgId(org.getId());
            if (roleId==null) {
                buildSysOrgAdmin(sysOrg.getId(), sysOrg.getPackageId());
            }else{
                Set<Integer> oldMenuIds = sysOrgPackageService.buildPackageMenuIds(org.getPackageId());
                Set<Integer> newMenuIds = sysOrgPackageService.buildPackageMenuIds(sysOrg.getPackageId());
                Set<Integer> delete = oldMenuIds.stream().filter(id -> !newMenuIds.contains(id)).collect(Collectors.toSet());
                Set<Integer> add = newMenuIds.stream().filter(id -> !oldMenuIds.contains(id)).collect(Collectors.toSet());
                roleService.clearOrgPackageCache(add,delete,org.getId());
            }
        }
        org.setName(sysOrg.getName());
        org.setStatus(sysOrg.getStatus());
        org.setPackageId(sysOrg.getPackageId());
        sysOrgRepository.save(org);
    }

    /**
     * 批量删除多租户
     * 
     * @param ids 需要删除的多租户主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysOrgByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteSysOrgById(id);
        }
    }

    /**
     * 删除多租户信息
     * 
     * @param id 多租户主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysOrgById(Integer id) {
        sysOrgRepository.logicDeleteById(id);
        userService.deleteUserByOrgId(id);
    }


    @Override
    public void download(List<SysOrgDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysOrgDto sysOrg : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  sysOrg.getId());
            map.put("name",  sysOrg.getName());
            map.put("createBy",  sysOrg.getCreateBy());
            map.put("createTime",  sysOrg.getCreateTime());
            map.put("updateBy",  sysOrg.getUpdateBy());
            map.put("updateTime",  sysOrg.getUpdateTime());
            map.put("deleted",  sysOrg.getDeleted());
            map.put("status",  sysOrg.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SysOrgDto> getEnableOrgList() {
        List<SysOrg> list = sysOrgRepository.findAllByStatus(CommonStatusEnum.ENABLE.getCode());
        return list==null ? null : list.stream().map(sysOrgMapper::toDto).toList();
    }

    @Override
    public void updateOrgStatus(SysOrg resources) {
        sysOrgRepository.updateOrgStatus(resources.getId(),resources.getStatus());
        userService.updateUserStatusByOrgId(resources.getId(), resources.getStatus());
    }

    private void validSysOrg(SysOrg sysOrg) {
        String name = sysOrg.getName();
        if (StringUtils.isBlank(name)) {
            throw new BadRequestException("租户名称不能为空");
        }
        if (sysOrg.getPackageId()==null) {
            throw new BadRequestException("套餐不能为空");
        }
        SysOrg org = sysOrgRepository.findByName(name);
        if (org==null) {
            return;
        }
        if (sysOrg.getId()==null || !org.getId().equals(sysOrg.getId())) {
            throw new BadRequestException("租户名称已存在");
        }
    }


    private void buildSysOrgAdmin(Integer id, Integer packageId) {
        List<SysOrgPackageMenus> menus = sysOrgPackageMenusRepository.findByPackageId(packageId);
        if (menus==null || menus.isEmpty()) {
            return;
        }
        Role role = new Role();
        role.setName("租户管理员");
        role.setRoleKey("org_admin");
        role.setOrgId(id);
        role.setRoleSort(1);
        role.setStatus(CommonStatusEnum.ENABLE.getCode());
        role.setDataScope(DataScopeEnum.ALL.getValue());
        roleRepository.save(role);
        menus.forEach(s->{
            roleRepository.insertRoleMenu(s.getMenuId(), role.getId(),id);
                });
    }
}
