package cc.efit.call.biz.service.impl;

import cc.efit.call.api.vo.task.IndexStatisticsInfo;
import cc.efit.utils.BigDecimalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallSystemStatisticsMapper;
import cc.efit.call.api.repository.CallSystemStatisticsRepository;
import cc.efit.call.biz.service.dto.CallSystemStatisticsDto;
import cc.efit.call.biz.service.dto.CallSystemStatisticsQueryCriteria;
import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.call.biz.service.CallSystemStatisticsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 呼叫任务系统维度统计表Service业务层处理
 * 
 * @author across
 * @date 2025-10-22
 */
@Service
@RequiredArgsConstructor
public class CallSystemStatisticsServiceImpl implements CallSystemStatisticsService  {

    private final CallSystemStatisticsRepository callSystemStatisticsRepository;
    private final CallSystemStatisticsMapper callSystemStatisticsMapper;

    @Override
    public PageResult<CallSystemStatisticsDto> queryAll(CallSystemStatisticsQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallSystemStatistics> page = callSystemStatisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callSystemStatisticsMapper::toDto));
    }

    @Override
    public List<CallSystemStatisticsDto> queryAll(CallSystemStatisticsQueryCriteria criteria){
        return callSystemStatisticsMapper.toDto(callSystemStatisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询呼叫任务系统维度统计表
     * 
     * @param id 呼叫任务系统维度统计表主键
     * @return 呼叫任务系统维度统计表
     */
    @Override
    public CallSystemStatisticsDto selectCallSystemStatisticsById(Integer id)  {
        return callSystemStatisticsMapper.toDto(callSystemStatisticsRepository.findById(id).orElseGet(CallSystemStatistics::new));
    }


    /**
     * 新增呼叫任务系统维度统计表
     * 
     * @param callSystemStatistics 呼叫任务系统维度统计表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallSystemStatistics(CallSystemStatistics callSystemStatistics) {
        callSystemStatisticsRepository.save(callSystemStatistics);
    }

    /**
     * 修改呼叫任务系统维度统计表
     * 
     * @param callSystemStatistics 呼叫任务系统维度统计表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallSystemStatistics(CallSystemStatistics callSystemStatistics) {
        callSystemStatisticsRepository.save(callSystemStatistics);
    }

    /**
     * 批量删除呼叫任务系统维度统计表
     * 
     * @param ids 需要删除的呼叫任务系统维度统计表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallSystemStatisticsByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallSystemStatisticsById(id);
        }
    }

    /**
     * 删除呼叫任务系统维度统计表信息
     * 
     * @param id 呼叫任务系统维度统计表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallSystemStatisticsById(Integer id) {
        callSystemStatisticsRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallSystemStatisticsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallSystemStatisticsDto callSystemStatistics : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callSystemStatistics.getId());
            map.put("callDate",  callSystemStatistics.getCallDate());
            map.put("callTime",  callSystemStatistics.getCallTime());
            map.put("totalCustomers",  callSystemStatistics.getTotalCustomers());
            map.put("calledCustomers",  callSystemStatistics.getCalledCustomers());
            map.put("connectCount",  callSystemStatistics.getConnectCount());
            map.put("duration",  callSystemStatistics.getDuration());
            map.put("concurrent",  callSystemStatistics.getConcurrent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public IndexStatisticsInfo getSystemStatisticsInfo() {
        LocalDate startTime = LocalDate.now();
        LocalDate endTime = startTime.plusDays(1);
        List<CallSystemStatistics> all = callSystemStatisticsRepository.findByCallDateBetween(startTime, endTime);
        Map<String, CallSystemStatistics> map = new HashMap<>();
        if (all != null && !all.isEmpty()) {
            map = all.stream().collect(Collectors.toMap(CallSystemStatistics::getCallTime, Function.identity()));
        }
        //统计信息 9-21 点
        /*
         * 时间
         */
        List<String> timeList = new ArrayList<>();
        /*
         * 呼叫量
         */
        List<Integer> callList = new ArrayList<>();
        /*
         * 接通量
         */
        List<Integer> answerList = new ArrayList<>();
        /*
         * 接通率 保留整数
         */
        List<Integer> rateList = new ArrayList<>();
        /*
         * 并发数
         */
        List<Integer> concurrentList = new ArrayList<>();
        for (int i=9;i<22;i++){
            String time = buildTime(i);
            timeList.add(time);
            CallSystemStatistics callSystemStatistics = map.get(time);
            if (callSystemStatistics != null) {
                callList.add(callSystemStatistics.getCalledCustomers());
                answerList.add(callSystemStatistics.getConnectCount());
                rateList.add(BigDecimalUtils.dividePercent(callSystemStatistics.getConnectCount(), callSystemStatistics.getCalledCustomers()));
                concurrentList.add(callSystemStatistics.getConcurrent());
            }else{
                callList.add(0);
                answerList.add(0);
                rateList.add(0);
                concurrentList.add(0);
            }
        }
        return new IndexStatisticsInfo(timeList, callList, answerList, rateList, concurrentList);
    }

    private String buildTime(int i) {
        return i<10?"0"+i + ":00":i+":00";
    }

}
