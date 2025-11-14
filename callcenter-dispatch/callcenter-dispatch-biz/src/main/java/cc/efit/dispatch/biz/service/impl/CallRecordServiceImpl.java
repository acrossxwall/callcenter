package cc.efit.dispatch.biz.service.impl;

import cc.efit.call.api.constants.CallStatisticsConstants;
import cc.efit.call.api.domain.CallRecord;
import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.repository.CallCustomerRepository;
import cc.efit.call.api.repository.CallRecordRepository;
import cc.efit.core.config.NfsPathConfig;
import cc.efit.utils.DateUtil;
import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dispatch.biz.intelligent.IntelligentAdjustConcurrent;
import cc.efit.dispatch.biz.intention.IntentionLevelHandler;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.core.InteractiveRecord;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.api.req.LineInfoReq;
import cc.efit.dispatch.biz.config.RecordPathConfig;
import cc.efit.dispatch.biz.service.CallRecordService;
import cc.efit.dispatch.biz.service.CallTaskService;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import java.util.List;
import java.util.Objects;

import static cc.efit.process.api.constants.DialogueRedisConstant.DIALOGUE_SESSION_KEY;

@Service
@Slf4j
public class CallRecordServiceImpl implements CallRecordService {
    @Autowired
    private CallTaskService taskService;
    @Autowired
    private CallRecordRepository callRecordRepository;
    @Autowired
    private RecordPathConfig recordPathConfig;
    @Autowired
    private NfsPathConfig nfsPathConfig;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IntentionLevelHandler intentionLevelHandler;
    @Autowired
    private IntelligentAdjustConcurrent intelligentAdjustConcurrent;
    @Autowired
    private CallCustomerRepository customerRepository;
    @Override
    public void handlerCallRecord(String json) throws Exception {
        DialProcessSession session = JsonUtils.parseObject(json, DialProcessSession.class);
        handlerCallRecord(session);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerCallRecord(DialProcessSession session) throws Exception {
        String callId = session.getCallId();
        CallCustomerInfoReq customer = session.getCustomer();
        CallTask task = taskService.findCallTaskFromDb(customer.taskId());
        if(task == null){
            log.info("callId:{} task is null",callId);
            return;
        }
        if (session.isReleaseSemaphore()) {
            intelligentAdjustConcurrent.releaseLineReduceConcurrent(task.getLineId(),task.getDeptId(),task.getId());
        }

        LineInfoReq lineInfo = session.getLineInfo();
        CallRecord record = new CallRecord();
        record.setTaskId(customer.taskId());
        record.setTaskName(customer.taskName());
        record.setCallTemplateId(customer.callTemplateId());
        record.setCallTemplateName(customer.callTemplateName());
        record.setCustomerId(customer.id());
        record.setCallId(callId);
        if (lineInfo!= null) {
            record.setLineId(lineInfo.lineId());
            record.setLineName(lineInfo.lineName());
            record.setCallNumber(lineInfo.callNumber());
        }
        record.setPhone(customer.phone());
        record.setCustomerInfo(customer.customerInfo());
        if (!session.isRiskHandler()) {
            record.setCallTime(session.getCallTime());
            record.setAnswerTime(session.getAnswerTime());
            record.setHangupTime(session.getHangupTime());
            record.setHangupReason(session.getHangupReason());
            record.setRingSeconds((int) (session.getAnswerTime()==null?
                    DateUtil.diffSeconds(session.getCallTime(),session.getHangupTime())
                    : DateUtil.diffSeconds(session.getCallTime(),session.getAnswerTime())));
            Integer callStatus ;
            if (session.getAnswerTime()!=null) {
                //接听处理录音 交互记录 交互轮次 意向等级 等
                record.setDuration((int) DateUtil.diffSeconds(session.getAnswerTime(),session.getHangupTime()));
                record.setStatus(CallCustomerStatusEnum.CustomerCallStatus.CONNECT.getStatus());
                buildCallRecordPath(record,session.getCallUuid());
                buildInteractiveRecord(record);
                buildIntentionLevel(record);
                callStatus = CallCustomerStatusEnum.CustomerCallStatus.CONNECT.getStatus() ;
                String callConnectKey = CallStatisticsConstants.CALL_STATISTICS_CONNECT_KEY.formatted(task.getId());
                incrementCallTaskStatistics(callConnectKey,1L);
                String durationKey = CallStatisticsConstants.CALL_STATISTICS_DURATION_KEY.formatted(task.getId());
                incrementCallTaskStatistics(durationKey,record.getDuration());
            }else{
                record .setDuration(0);
                record.setStatus(CallCustomerStatusEnum.CustomerCallStatus.REJECT.getStatus());
                callStatus = CallCustomerStatusEnum.CustomerCallStatus.REJECT.getStatus() ;
            }
            if (customer.id()!=null) {
                customerRepository.updateCustomerCallStatus(customer.id(), callStatus, CallCustomerStatusEnum.CustomerStatus.FINISH.getStatus());
            }
        }else{
            record.setStatus(session.getCallStatus());
        }
        String callCountKey = CallStatisticsConstants.CALL_STATISTICS_CALLED_KEY.formatted(task.getId());
        incrementCallTaskStatistics(callCountKey,1L);
        //构建公共字段和task保持一致
        record.setOrgId(task.getOrgId());
        record.setDeptId(task.getDeptId());
        record.setUserId(task.getUserId());
        callRecordRepository.save(record);
    }

    private void buildIntentionLevel(CallRecord record) {
        String callId = record.getCallId();
        log.info("开始处理意向等级记录 callId:{}",callId);
        Integer callTemplateId = record.getCallTemplateId();
        DialogueProcessSession processSession = redisUtils.get( DIALOGUE_SESSION_KEY.formatted(callId), DialogueProcessSession.class);
        record.setInteractiveCount(processSession.getInteractiveCount());
        intentionLevelHandler.processCallRecordIntentionLevel(callTemplateId, processSession, record);
    }

    private void  incrementCallTaskStatistics(String redisKey,long count){
        redisUtils.increment(redisKey,count);
    }

    private void buildInteractiveRecord(CallRecord record) {
        String callId = record.getCallId();
        log.info("开始处理交互记录 callId:{}",callId);
        List <Object> list =redisUtils.lGet(DialKeyConstants.DIAL_SESSION_INTERACTIVE_KEY.formatted(callId),0,-1);
        if (list==null || list.isEmpty()) {
            log.info("交互记录为空 callId:{}",callId);
            return;
        }
        List<InteractiveRecord> result = list.stream().filter(Objects::nonNull).map(o-> JsonUtils.parseObject(o.toString(),InteractiveRecord.class)).toList();
        record.setInteractiveRecord(result);
        List<String> tags = result.stream().filter(s->s.flowData()!=null).map(s-> s.flowData().nodeLabel()).toList();
        record.setTags(tags);
    }

    private void buildCallRecordPath(CallRecord record, String callUuid) {
        String callId = record.getCallId();
        log.info("开始处理录音文件的路径:{},uuid:{}",callId,callUuid);
        String date = DateUtil.localDateTimeFormatYMd(record.getCallTime());
        String fsPath = recordPathConfig.getFs().formatted(date, callUuid);
        File file = new File(fsPath);
        if (!file.exists()) {
            log.info("callId:{}录音文件不存在:{}",callId,fsPath);
            return;
        }
        String targetPath = recordPathConfig.getDb().formatted(record.getOrgId(), date, callId);
        File target =  FileUtil.copyFile(fsPath,nfsPathConfig.getBase() + targetPath);
        if (target.exists() && target.length()==file.length()){
            log.info("callId:{}录音文件处理成功:{}",callId,targetPath);
            record.setRecordPath( targetPath);
        } else {
            //TODO 发送异常通知 或者写入错误到数据库，同一处理
        }
    }
}
