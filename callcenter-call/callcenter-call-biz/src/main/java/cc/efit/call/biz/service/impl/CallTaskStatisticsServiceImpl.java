package cc.efit.call.biz.service.impl;

import cc.efit.call.api.vo.task.TaskStatisticsInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallTaskStatisticsMapper;
import cc.efit.call.api.repository.CallTaskStatisticsRepository;
import cc.efit.call.biz.service.dto.CallTaskStatisticsDto;
import cc.efit.call.biz.service.dto.CallTaskStatisticsQueryCriteria;
import cc.efit.call.api.domain.CallTaskStatistics;
import cc.efit.call.biz.service.CallTaskStatisticsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 呼叫任务统计表表Service业务层处理
 * 
 * @author across
 * @date 2025-10-15
 */
@Service
@RequiredArgsConstructor
public class CallTaskStatisticsServiceImpl implements CallTaskStatisticsService  {

    private final CallTaskStatisticsRepository callTaskStatisticsRepository;
    private final CallTaskStatisticsMapper callTaskStatisticsMapper;

    @Override
    public PageResult<CallTaskStatisticsDto> queryAll(CallTaskStatisticsQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallTaskStatistics> page = callTaskStatisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callTaskStatisticsMapper::toDto));
    }

    @Override
    public List<CallTaskStatisticsDto> queryAll(CallTaskStatisticsQueryCriteria criteria){
        return callTaskStatisticsMapper.toDto(callTaskStatisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询呼叫任务统计表表
     * 
     * @param id 呼叫任务统计表表主键
     * @return 呼叫任务统计表表
     */
    @Override
    public CallTaskStatisticsDto selectCallTaskStatisticsById(Integer id)  {
        return callTaskStatisticsMapper.toDto(callTaskStatisticsRepository.findById(id).orElseGet(CallTaskStatistics::new));
    }


    /**
     * 新增呼叫任务统计表表
     * 
     * @param callTaskStatistics 呼叫任务统计表表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallTaskStatistics(CallTaskStatistics callTaskStatistics) {
        callTaskStatisticsRepository.save(callTaskStatistics);
    }

    /**
     * 修改呼叫任务统计表表
     * 
     * @param callTaskStatistics 呼叫任务统计表表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallTaskStatistics(CallTaskStatistics callTaskStatistics) {
        callTaskStatisticsRepository.save(callTaskStatistics);
    }

    /**
     * 批量删除呼叫任务统计表表
     * 
     * @param ids 需要删除的呼叫任务统计表表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTaskStatisticsByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallTaskStatisticsById(id);
        }
    }

    /**
     * 删除呼叫任务统计表表信息
     * 
     * @param id 呼叫任务统计表表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTaskStatisticsById(Integer id) {
        callTaskStatisticsRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallTaskStatisticsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallTaskStatisticsDto callTaskStatistics : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callTaskStatistics.getId());
            map.put("taskId",  callTaskStatistics.getTaskId());
            map.put("callDate",  callTaskStatistics.getCallDate());
            map.put("totalCustomers",  callTaskStatistics.getTotalCustomers());
            map.put("calledCustomers",  callTaskStatistics.getCalledCustomers());
            map.put("connectCount",  callTaskStatistics.getConnectCount());
            map.put("duration",  callTaskStatistics.getDuration());
            map.put("smsCount",  callTaskStatistics.getSmsCount());
            map.put("smsSuccessCount",  callTaskStatistics.getSmsSuccessCount());
            map.put("smsBillCount",  callTaskStatistics.getSmsBillCount());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TaskStatisticsInfo selectCallTaskStatisticsInfo() {
        LocalDate startTime = LocalDate.now();
        LocalDate endTime = startTime.plusDays(1);
        return callTaskStatisticsRepository.selectTaskStatisticsInfo(startTime,endTime);
    }

}
