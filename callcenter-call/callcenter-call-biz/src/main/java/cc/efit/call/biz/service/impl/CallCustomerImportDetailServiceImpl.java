package cc.efit.call.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallCustomerImportDetailMapper;
import cc.efit.call.biz.repository.CallCustomerImportDetailRepository;
import cc.efit.call.biz.service.dto.CallCustomerImportDetailDto;
import cc.efit.call.biz.service.dto.CallCustomerImportDetailQueryCriteria;
import cc.efit.call.biz.domain.CallCustomerImportDetail;
import cc.efit.call.biz.service.CallCustomerImportDetailService;
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
 * 客户名单批次详细表Service业务层处理
 * 
 * @author across
 * @date 2025-09-12
 */
@Service
@RequiredArgsConstructor
public class CallCustomerImportDetailServiceImpl implements CallCustomerImportDetailService  {

    private final CallCustomerImportDetailRepository callCustomerImportDetailRepository;
    private final CallCustomerImportDetailMapper callCustomerImportDetailMapper;

    @Override
    public PageResult<CallCustomerImportDetailDto> queryAll(CallCustomerImportDetailQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallCustomerImportDetail> page = callCustomerImportDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callCustomerImportDetailMapper::toDto));
    }

    @Override
    public List<CallCustomerImportDetailDto> queryAll(CallCustomerImportDetailQueryCriteria criteria){
        return callCustomerImportDetailMapper.toDto(callCustomerImportDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询客户名单批次详细表
     * 
     * @param id 客户名单批次详细表主键
     * @return 客户名单批次详细表
     */
    @Override
    public CallCustomerImportDetailDto selectCallCustomerImportDetailById(Integer id)  {
        return callCustomerImportDetailMapper.toDto(callCustomerImportDetailRepository.findById(id).orElseGet(CallCustomerImportDetail::new));
    }


    /**
     * 新增客户名单批次详细表
     * 
     * @param callCustomerImportDetail 客户名单批次详细表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallCustomerImportDetail(CallCustomerImportDetail callCustomerImportDetail) {
        callCustomerImportDetailRepository.save(callCustomerImportDetail);
    }

    /**
     * 修改客户名单批次详细表
     * 
     * @param callCustomerImportDetail 客户名单批次详细表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallCustomerImportDetail(CallCustomerImportDetail callCustomerImportDetail) {
        callCustomerImportDetailRepository.save(callCustomerImportDetail);
    }

    /**
     * 批量删除客户名单批次详细表
     * 
     * @param ids 需要删除的客户名单批次详细表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallCustomerImportDetailByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallCustomerImportDetailById(id);
        }
    }

    /**
     * 删除客户名单批次详细表信息
     * 
     * @param id 客户名单批次详细表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallCustomerImportDetailById(Integer id) {
        callCustomerImportDetailRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallCustomerImportDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallCustomerImportDetailDto callCustomerImportDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("任务id",  callCustomerImportDetail.getTaskId());
            map.put("手机号码",  callCustomerImportDetail.getPhone());
            map.put("批次号",  callCustomerImportDetail.getBatchNo());
            map.put("原因",  callCustomerImportDetail.getReason());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
