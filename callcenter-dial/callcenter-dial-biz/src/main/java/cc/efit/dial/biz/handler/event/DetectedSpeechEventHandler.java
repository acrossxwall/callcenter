package cc.efit.dial.biz.handler.event;

import cc.efit.utils.DateUtil;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EslEventHeaderNames;
import cc.efit.esl.core.event.EventNames;
import cc.efit.timer.core.TimerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static cc.efit.dial.api.constant.DialKeyConstants.DELAY_TASK_KEY;

@Service("detectedSpeechEventHandler")
@Slf4j
public class DetectedSpeechEventHandler extends AbstractEventHandler {
    @Autowired
    private TimerService timerService;

    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("detected speech handler callId:{}",callId);
        //asr 识别结果，由于asr识别是实时识别的，要记录下来，如果播音完成需要去识别才记录
        List<String> asrResult = event.getEventBodyLines();
        boolean input = false;
        if (asrResult != null && !asrResult.isEmpty()) {
            log.info("detected speech handler callId:{} asr:{}",callId,asrResult);
            session.setAsrResult(asrResult);
            input = true;
        }
        if (session.getStatus()== ProcessSessionStatusEnum.WAIT_CUSTOMER_INPUT &&
              "detected-speech".equalsIgnoreCase(event.getEventHeaders().get(EslEventHeaderNames.SPEECH_TYPE)) ){
            //状态为等待输入且asr识别到结果，设置状态为用户输入
            session.setStatus(ProcessSessionStatusEnum.CUSTOMER_INPUT);
            //清空定时任务，全局设置30s等待超时，该定时任务在WAIT_CUSTOMER_INPUT状态时设置
            String delayTask = DELAY_TASK_KEY.formatted(callId);
            timerService.cancel(delayTask);
        }else if (session.getStatus()== ProcessSessionStatusEnum.PLAYING && session.isEnableInterrupt()) {
            //打断开启 可以判断设置前多少秒不允许打断
            long seconds = DateUtil.diffSeconds(session.getPlayStartTime(), LocalDateTime.now());
            //当没有设置或者为空
            int setVal = session.getInterruptSeconds();
            if ( seconds >= setVal && input) {
                getFsApiCommand().pausePlayChannel(session.getCallUuid());
                //把当前通话的状态调整为等待客户输入
                session.setStatus(ProcessSessionStatusEnum.WAIT_CUSTOMER_INPUT);
            }
        }
    }

    @Override
    public String getEventName() {
        return EventNames.DETECTED_SPEECH;
    }
}
