package cc.efit.dispatch.biz.service;

import cc.efit.call.api.domain.Line;
import cc.efit.call.api.repository.LineRepository;
import cc.efit.call.api.vo.line.CallLineInfo;
import cc.efit.dispatch.api.constant.DispatchRedisKeyConstant;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cc.efit.call.api.constants.DispatchKeyConstants.DISPATCH_LINE_KEY;

@Service
public class TaskCommonService {

    @Autowired
    private Redisson redisson;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private LineRepository lineRepository;

    public RSemaphore getTaskDispatchConcurrencySemaphore(Integer taskId) {
        String concurrentKey =  DispatchRedisKeyConstant .DISPATCH_TASK_CONCURRENCY.formatted(taskId);
        return redisson.getSemaphore(concurrentKey) ;
    }

    public CallLineInfo getTaskCallLineInfo(Integer lineId) {
        String lineKey = DISPATCH_LINE_KEY.formatted(lineId);
        return redisUtils.get(lineKey, CallLineInfo.class);
    }

    public CallLineInfo buildLineInfoToRedis(Integer lineId) {
        Line line = lineRepository.findById(lineId).orElse(null);
        if (line==null) {
            throw new RuntimeException("线路不存在");
        }
        CallLineInfo lineInfo = new CallLineInfo(
                line.getId(),line.getRealm(),line.getPort(),line.getCallNumber(),
                line.getCallPrefix(),line.getLineName(),line.getGatewayName()
        );
        redisUtils.set(DISPATCH_LINE_KEY.formatted(line.getId()), JsonUtils.toJsonString( lineInfo));
        return lineInfo;
    }
}
