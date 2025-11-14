package cc.efit.call.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallRecordMapper;
import cc.efit.call.api.repository.CallRecordRepository;
import cc.efit.call.biz.service.dto.CallRecordDto;
import cc.efit.call.biz.service.dto.CallRecordQueryCriteria;
import cc.efit.call.api.domain.CallRecord;
import cc.efit.call.biz.service.CallRecordService;
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
 * 呼叫记录表Service业务层处理
 * 
 * @author across
 * @date 2025-09-26
 */
@Service
@RequiredArgsConstructor
public class CallRecordServiceImpl implements CallRecordService  {

    private final CallRecordRepository callRecordRepository;
    private final CallRecordMapper callRecordMapper;

    @Override
    public PageResult<CallRecordDto> queryAll(CallRecordQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallRecord> page = callRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callRecordMapper::toDto));
    }

    @Override
    public List<CallRecordDto> queryAll(CallRecordQueryCriteria criteria){
        return callRecordMapper.toDto(callRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询呼叫记录表
     * 
     * @param id 呼叫记录表主键
     * @return 呼叫记录表
     */
    @Override
    public CallRecordDto selectCallRecordById(Integer id)  {
        return callRecordMapper.toDto(callRecordRepository.findById(id).orElseGet(CallRecord::new));
    }


    /**
     * 新增呼叫记录表
     * 
     * @param callRecord 呼叫记录表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallRecord(CallRecord callRecord) {
        callRecordRepository.save(callRecord);
    }

    /**
     * 修改呼叫记录表
     * 
     * @param callRecord 呼叫记录表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallRecord(CallRecord callRecord) {
        callRecordRepository.save(callRecord);
    }

    /**
     * 批量删除呼叫记录表
     * 
     * @param ids 需要删除的呼叫记录表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallRecordByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallRecordById(id);
        }
    }

    /**
     * 删除呼叫记录表信息
     * 
     * @param id 呼叫记录表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallRecordById(Integer id) {
        callRecordRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallRecordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallRecordDto callRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callRecord.getId());
            map.put("taskId",  callRecord.getTaskId());
            map.put("taskName",  callRecord.getTaskName());
            map.put("callTemplateId",  callRecord.getCallTemplateId());
            map.put("callTemplateName",  callRecord.getCallTemplateName());
            map.put("customerId",  callRecord.getCustomerId());
            map.put("callId",  callRecord.getCallId());
            map.put("lineId",  callRecord.getLineId());
            map.put("lineName",  callRecord.getLineName());
            map.put("callNumber",  callRecord.getCallNumber());
            map.put("phone",  callRecord.getPhone());
            map.put("customerInfo",  callRecord.getCustomerInfo());
            map.put("callTime",  callRecord.getCallTime());
            map.put("answerTime",  callRecord.getAnswerTime());
            map.put("hangupTime",  callRecord.getHangupTime());
            map.put("ringSeconds",  callRecord.getRingSeconds());
            map.put("hangupReason",  callRecord.getHangupReason());
            map.put("intentionLevel",  callRecord.getIntentionLevel());
            map.put("intentionName",  callRecord.getIntentionName());
            map.put("duration",  callRecord.getDuration());
            map.put("status",  callRecord.getStatus());
            map.put("recordPath",  callRecord.getRecordPath());
            map.put("interactiveRecord",  callRecord.getInteractiveRecord());
            map.put("interactiveCount",  callRecord.getInteractiveCount());
            map.put("tags",  callRecord.getTags());
            map.put("createBy",  callRecord.getCreateBy());
            map.put("updateBy",  callRecord.getUpdateBy());
            map.put("createTime",  callRecord.getCreateTime());
            map.put("updateTime",  callRecord.getUpdateTime());
            map.put("deleted",  callRecord.getDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
