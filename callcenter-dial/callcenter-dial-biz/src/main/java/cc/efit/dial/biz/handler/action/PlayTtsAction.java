package cc.efit.dial.biz.handler.action;

import cc.efit.core.enums.YesNoEnum;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.PlayTtsActionData;
import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("playTtsAction")
@Slf4j
public class PlayTtsAction extends AbstractActionHandler {
    @Override
    protected String doAction(DialProcessSession session, BaseActionData actionData) throws ProcessActionException {
        String callId = session.getCallId();
        String callUuid = session.getCallUuid();
        session.setStatus(ProcessSessionStatusEnum.PLAYING);
        PlayTtsActionData playFileAction = (PlayTtsActionData) actionData;
        session.setEnableInterrupt(YesNoEnum.YES.getCode().equals(playFileAction.getEnableInterrupt()));
        log.info("callId:{},callUuid:{},play tts:{}", callId, callUuid, playFileAction.getContent());
        //TODO 生成tts文件 待实现
        String filePath = null;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ProcessActionException("播音文件不存在");
        }
        getFsApiCommand().playFile( callUuid, filePath);
        return playFileAction.getContent();
    }

    @Override
    public ProcessResActionEnum getActionEnum() {
        return ProcessResActionEnum.PLAY_TTS;
    }
}
