package cc.efit.call.biz.service.impl;

import cc.efit.call.api.enums.CustomerSourceEnum;
import cc.efit.call.biz.domain.BlackInfo;
import cc.efit.call.biz.service.dto.BlackInfoDto;
import cc.efit.call.biz.service.mapstruct.BlackInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.repository.BlackInfoRepository;
import cc.efit.call.biz.service.dto.BlackInfoQueryCriteria;
import cc.efit.call.biz.service.BlackInfoService;
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
 * 黑名单库表Service业务层处理
 * 
 * @author across
 * @date 2025-08-27
 */
@Service
@RequiredArgsConstructor
public class BlackInfoServiceImpl implements BlackInfoService {

    private final BlackInfoRepository blackInfoRepository;
    private final BlackInfoMapper blackInfoMapper;

    @Override
    public PageResult<BlackInfoDto> queryAll(BlackInfoQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<BlackInfo> page = blackInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(blackInfoMapper::toDto));
    }

    @Override
    public List<BlackInfoDto> queryAll(BlackInfoQueryCriteria criteria){
        return blackInfoMapper.toDto(blackInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询黑名单库表
     * 
     * @param id 黑名单库表主键
     * @return 黑名单库表
     */
    @Override
    public BlackInfoDto selectBlackInfoById(Integer id)  {
        return blackInfoMapper.toDto(blackInfoRepository.findById(id).orElseGet(BlackInfo::new));
    }


    /**
     * 新增黑名单库表
     * 
     * @param blackInfo 黑名单库表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBlackInfo(BlackInfo blackInfo) {
        blackInfo.setSource(CustomerSourceEnum.PAGE.getCode());
        blackInfoRepository.save(blackInfo);
    }

    /**
     * 修改黑名单库表
     * 
     * @param blackInfo 黑名单库表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBlackInfo(BlackInfo blackInfo) {
        blackInfoRepository.save(blackInfo);
    }

    /**
     * 批量删除黑名单库表
     * 
     * @param ids 需要删除的黑名单库表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlackInfoByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteBlackInfoById(id);
        }
    }

    /**
     * 删除黑名单库表信息
     * 
     * @param id 黑名单库表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlackInfoById(Integer id) {
        blackInfoRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<BlackInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BlackInfoDto blackInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  blackInfo.getId());
            map.put("name",  blackInfo.getName());
            map.put("phone",  blackInfo.getPhone());
            map.put("source",  blackInfo.getSource());
            map.put("expireTime",  blackInfo.getExpireTime());
            map.put("createBy",  blackInfo.getCreateBy());
            map.put("createTime",  blackInfo.getCreateTime());
            map.put("updateBy",  blackInfo.getUpdateBy());
            map.put("updateTime",  blackInfo.getUpdateTime());
            map.put("deleted",  blackInfo.getDeleted());
            map.put("remark",  blackInfo.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
