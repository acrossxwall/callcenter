package cc.efit.call.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallCustomerBatchMapper;
import cc.efit.call.biz.repository.CallCustomerBatchRepository;
import cc.efit.call.biz.service.dto.CallCustomerBatchDto;
import cc.efit.call.biz.service.dto.CallCustomerBatchQueryCriteria;
import cc.efit.call.biz.domain.CallCustomerBatch;
import cc.efit.call.biz.service.CallCustomerBatchService;
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
 * 客户名单批次表Service业务层处理
 * 
 * @author across
 * @date 2025-09-10
 */
@Service
@RequiredArgsConstructor
public class CallCustomerBatchServiceImpl implements CallCustomerBatchService  {

    private final CallCustomerBatchRepository callCustomerBatchRepository;
    private final CallCustomerBatchMapper callCustomerBatchMapper;

    @Override
    public PageResult<CallCustomerBatchDto> queryAll(CallCustomerBatchQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallCustomerBatch> page = callCustomerBatchRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callCustomerBatchMapper::toDto));
    }

    @Override
    public List<CallCustomerBatchDto> queryAll(CallCustomerBatchQueryCriteria criteria){
        return callCustomerBatchMapper.toDto(callCustomerBatchRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询客户名单批次表
     * 
     * @param id 客户名单批次表主键
     * @return 客户名单批次表
     */
    @Override
    public CallCustomerBatchDto selectCallCustomerBatchById(Integer id)  {
        return callCustomerBatchMapper.toDto(callCustomerBatchRepository.findById(id).orElseGet(CallCustomerBatch::new));
    }


    /**
     * 新增客户名单批次表
     * 
     * @param callCustomerBatch 客户名单批次表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallCustomerBatch(CallCustomerBatch callCustomerBatch) {
        callCustomerBatchRepository.save(callCustomerBatch);
    }

    /**
     * 修改客户名单批次表
     * 
     * @param callCustomerBatch 客户名单批次表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallCustomerBatch(CallCustomerBatch callCustomerBatch) {
        callCustomerBatchRepository.save(callCustomerBatch);
    }

    /**
     * 批量删除客户名单批次表
     * 
     * @param ids 需要删除的客户名单批次表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallCustomerBatchByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallCustomerBatchById(id);
        }
    }

    /**
     * 删除客户名单批次表信息
     * 
     * @param id 客户名单批次表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallCustomerBatchById(Integer id) {
        callCustomerBatchRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallCustomerBatchDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallCustomerBatchDto callCustomerBatch : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callCustomerBatch.getId());
            map.put("taskId",  callCustomerBatch.getTaskId());
            map.put("customerCount",  callCustomerBatch.getCustomerCount());
            map.put("batchNo",  callCustomerBatch.getBatchNo());
            map.put("createBy",  callCustomerBatch.getCreateBy());
            map.put("updateBy",  callCustomerBatch.getUpdateBy());
            map.put("createTime",  callCustomerBatch.getCreateTime());
            map.put("updateTime",  callCustomerBatch.getUpdateTime());
            map.put("deleted",  callCustomerBatch.getDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
