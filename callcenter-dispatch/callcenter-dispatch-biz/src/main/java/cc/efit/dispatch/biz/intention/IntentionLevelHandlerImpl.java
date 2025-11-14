package cc.efit.dispatch.biz.intention;

import cc.efit.call.api.domain.CallRecord;
import cc.efit.dialogue.api.constants.TemplateConstants;
import cc.efit.dialogue.api.enums.TemplateIntentionLevelTypeEnum;
import cc.efit.dialogue.api.vo.level.TemplateIntentionLevelInfo;
import cc.efit.dispatch.biz.rule.vo.RuleInfo;
import cc.efit.dispatch.biz.rule.RuleFactory;
import cc.efit.dispatch.biz.rule.RuleHandler;
import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.core.InteractiveRecord;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static cc.efit.dispatch.biz.utils.DispatchUtils.INTENTION_LEVEL_MAP;
import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_INTENTION_LEVEL_INFO;

@Service
@Slf4j
public class IntentionLevelHandlerImpl implements IntentionLevelHandler{
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RuleFactory ruleFactory;

    @Override
    public void processCallRecordIntentionLevel(Integer callTemplateId, DialogueProcessSession session, CallRecord record) {
        String callId = record.getCallId();
        log.info("callId:{},callTemplateId:{},开始匹配意向等级",callId,callTemplateId);
        //查询缓存的意向等级
        List <TemplateIntentionLevelInfo> list = getIntentionLevelLocalCacheOrRedis(callTemplateId);
        TemplateIntentionLevelInfo defaultLevel = list.stream().filter(s-> TemplateIntentionLevelTypeEnum.DEFAULT.getType().equals(s.type()))
                .findFirst().orElse(new TemplateIntentionLevelInfo(TemplateConstants.DEFAULT_INTENTION_LEVEL,TemplateConstants.DEFAULT_INTENTION_LEVEL_DESC,
                 TemplateIntentionLevelTypeEnum.DEFAULT.getType(),""));
        TemplateIntentionLevelInfo level = intentionLevelMatchResult(list.stream().filter(s->
                TemplateIntentionLevelTypeEnum.NORMAL.getType().equals(s.type())).toList(),
                session, record.getInteractiveRecord(),record.getDuration());
        if (level==null) {
            level = defaultLevel;
        }
        //设置意向等级
        record.setIntentionLevel(level.name());
        //设置意向等级描述
        record.setIntentionName(level.description());
    }

    private TemplateIntentionLevelInfo intentionLevelMatchResult(List<TemplateIntentionLevelInfo> list,
                                                                 DialogueProcessSession session,
                                                                 List<InteractiveRecord> interactiveRecord,
                                                                 int callDuration) {
        for (TemplateIntentionLevelInfo level : list) {
            //rule Content 是集合，满足所有条件才算此意向等级满足
            String levelRuleContent = level.ruleContent();
            if (StringUtils.isBlank(levelRuleContent)) {
                continue;
            }
            List<RuleInfo> ruleInfoList = JsonUtils.parseArray(levelRuleContent, RuleInfo.class);
            boolean match = true;
            for (RuleInfo ruleInfo : ruleInfoList) {
                log.info("callId:{}, rule info:{}", session.getCallId(), ruleInfo);
                match = ruleMatch(ruleInfo, session, interactiveRecord,callDuration);
                log.info("callId:{},结果是:{}",session.getCallId(),match);
                if (!match) {
                    //有一个规则不匹配当前意向等级就不命中
                    break;
                }
            }
            if (match) {
                return level;
            }
        }
        return null;
    }

    private boolean ruleMatch(RuleInfo ruleInfo, DialogueProcessSession session, List<InteractiveRecord> interactiveRecord,
                              int callDuration) {
        RuleHandler ruleHandler = ruleFactory.getRuleHandler(ruleInfo.condition());
        return ruleHandler.handleRuleMatch(ruleInfo, session, interactiveRecord,callDuration);
    }


    private List <TemplateIntentionLevelInfo> getIntentionLevelLocalCacheOrRedis(Integer callTemplateId){
        List<TemplateIntentionLevelInfo> levelList = INTENTION_LEVEL_MAP.get(callTemplateId);
        if(CollectionUtils.isEmpty(levelList)){
            String levelKey = TEMPLATE_INTENTION_LEVEL_INFO.formatted(callTemplateId);
            levelList = redisUtils.getList(levelKey, TemplateIntentionLevelInfo.class);
            INTENTION_LEVEL_MAP.put(callTemplateId,levelList);
        }
        return levelList;
    }
}
