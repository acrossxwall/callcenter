package cc.efit.dial.biz.listener;

import cc.efit.dial.biz.constants.DialConstants;
import cc.efit.dial.biz.handler.base.BaseEventHandler;
import cc.efit.dial.biz.handler.EventHandlerFactory;
import cc.efit.esl.core.Context;
import cc.efit.esl.core.IEslEventListener;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static cc.efit.dial.biz.constants.DialConstants.CALL_UUID_CALL_ID_MAP;


@Service
@Slf4j
public class FsEslEventListener implements IEslEventListener {
    @Autowired
    private EventHandlerFactory eventHandlerFactory;
    @Override
    public void onEslEvent(Context ctx, EslEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("收到了esl event:{}", event);
        }
        String eventName = event.getEventName();
        String json = JsonUtils.toJsonString(event);
        String callId = event.getCallId();
        String callUuid = event.getCallerUuid();
        if (callId==null) {
            //说明此时自定义的变量不存在，比如 asr 的事件
            callId = CALL_UUID_CALL_ID_MAP.get(callUuid);
        }
        log.info("收到事件:{},callId:{},{}", eventName,callId,json);
        if (callId == null) {
            //此时还为null,说明要么不是本服务拨打，要么是其他不处理的事件
            log.info("callId为空，不处理该事件" );
            return;
        }
        handleEvent(callId, event);
    }

    private void handleEvent(String callId, EslEvent event) {
        ReadWriteLock lock = DialConstants. lockMap.computeIfAbsent(callId, k -> new ReentrantReadWriteLock());
        lock.writeLock().lock();
        try {
            Long lastSeq = DialConstants.lastSeqMap.get(callId);
            long seq = event.getEventSequence();
            if (lastSeq != null && seq <= lastSeq) {return;}
            String eventName = event.getEventName();
            BaseEventHandler eventHandler = eventHandlerFactory.getEventHandler(eventName);
            if (eventHandler == null) {
                log.info("callId:{}没有找到对应的事件处理器:{}",callId, eventName);
                return;
            }
            eventHandler.processEventHandler(callId, event);
            DialConstants.lastSeqMap.put(callId, seq);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
