package cc.efit.call.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallTaskJobMapper;
import cc.efit.call.biz.repository.CallTaskJobRepository;
import cc.efit.call.biz.service.dto.CallTaskJobDto;
import cc.efit.call.biz.service.dto.CallTaskJobQueryCriteria;
import cc.efit.call.biz.domain.CallTaskJob;
import cc.efit.call.biz.service.CallTaskJobService;
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
 * 外呼任务job表Service业务层处理
 * 
 * @author across
 * @date 2025-10-20
 */
@Service
@RequiredArgsConstructor
public class CallTaskJobServiceImpl implements CallTaskJobService  {

    private final CallTaskJobRepository callTaskJobRepository;
    private final CallTaskJobMapper callTaskJobMapper;

    @Override
    public PageResult<CallTaskJobDto> queryAll(CallTaskJobQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallTaskJob> page = callTaskJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callTaskJobMapper::toDto));
    }

    @Override
    public List<CallTaskJobDto> queryAll(CallTaskJobQueryCriteria criteria){
        return callTaskJobMapper.toDto(callTaskJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询外呼任务job表
     * 
     * @param id 外呼任务job表主键
     * @return 外呼任务job表
     */
    @Override
    public CallTaskJobDto selectCallTaskJobById(Integer id)  {
        return callTaskJobMapper.toDto(callTaskJobRepository.findById(id).orElseGet(CallTaskJob::new));
    }


    /**
     * 新增外呼任务job表
     * 
     * @param callTaskJob 外呼任务job表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallTaskJob(CallTaskJob callTaskJob) {
        callTaskJobRepository.save(callTaskJob);
    }

    /**
     * 修改外呼任务job表
     * 
     * @param callTaskJob 外呼任务job表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallTaskJob(CallTaskJob callTaskJob) {
        callTaskJobRepository.save(callTaskJob);
    }

    /**
     * 批量删除外呼任务job表
     * 
     * @param ids 需要删除的外呼任务job表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTaskJobByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallTaskJobById(id);
        }
    }

    /**
     * 删除外呼任务job表信息
     * 
     * @param id 外呼任务job表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTaskJobById(Integer id) {
        callTaskJobRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallTaskJobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallTaskJobDto callTaskJob : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callTaskJob.getId());
            map.put("taskId",  callTaskJob.getTaskId());
            map.put("cronExpression",  callTaskJob.getCronExpression());
            map.put("executorHandler",  callTaskJob.getExecutorHandler());
            map.put("jobDesc",  callTaskJob.getJobDesc());
            map.put("type",  callTaskJob.getType());
            map.put("status",  callTaskJob.getStatus());
            map.put("jobId",  callTaskJob.getJobId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
