package cc.efit.process.biz.handler.utils;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.PlayFileActionData;
import cc.efit.process.api.action.PlayTtsActionData;
import cc.efit.process.api.enums.flow.TemplateVerbalTypeEnum;
import cc.efit.dialogue.api.vo.verbal.TemplateVerbalContentVo;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class TemplateVerbalUtils {

    public static List<BaseActionData> buildPlayVerbalContent(List<TemplateVerbalContentVo> contentList,
                                                              Map<String,String> customerInfo,Integer verbalType,
                                                              Integer enableInterrupt) {
        if (CollectionUtils.isEmpty(contentList)) {
            return null;
        }
        List<BaseActionData> actionDataList = new ArrayList<>();
        if (TemplateVerbalTypeEnum.RANDOM.getCode().equals(verbalType)) {
            //随机播报一个
            BaseActionData data = buildPlayActionData(customerInfo, contentList.get(RandomUtil.randomInt(0, contentList.size())),
                    enableInterrupt);
            if (data!=null) {
                actionDataList.add(data);
            }

        }else{
            //顺序播报
            contentList.forEach(s->{
                BaseActionData data = buildPlayActionData(customerInfo, s,enableInterrupt);
                if (data!=null) {
                    actionDataList.add(data);
                }
            });
        }
        return actionDataList;
    }

    public static BaseActionData buildPlayActionData(Map<String, String> customerInfo, TemplateVerbalContentVo content,
                                                     Integer enableInterrupt) {
        String verbalContent = content.verbalContent();
        if (StringUtils.isBlank(verbalContent)) {
            return null;
        }
        String recordFile = content.verbalFilePath();
        if (StringUtils.isNotBlank(recordFile)) {
            //播放音频文件
            PlayFileActionData playFileActionData = new PlayFileActionData();
            playFileActionData.setFilePath(recordFile);
            playFileActionData.setEnableInterrupt(enableInterrupt==null?0:enableInterrupt);
            playFileActionData.setContent(verbalContent);
            return playFileActionData;
        }else {
            //播放tts
            PlayTtsActionData playTtsActionData = new PlayTtsActionData();
            //对客户字典进行格式化
            playTtsActionData.setContent(buildPlayVerbalContent(verbalContent, customerInfo));
            playTtsActionData.setEnableInterrupt(enableInterrupt==null?0:enableInterrupt);
            return playTtsActionData;
        }
    }

    public static String buildPlayVerbalContent(String content ,
                           Map<String,String> customerInfo) {
        log.info("buildPlayVerbalContent:{},customer info:{}",content,customerInfo);
        return StringSubstitutor.replace(content, customerInfo);
    }

}
