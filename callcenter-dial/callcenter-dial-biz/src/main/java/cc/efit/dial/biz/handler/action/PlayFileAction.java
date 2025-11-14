package cc.efit.dial.biz.handler.action;

import cc.efit.core.config.NfsPathConfig;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.PlayFileActionData;
import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("playFileAction")
@Slf4j
public class PlayFileAction extends AbstractActionHandler {
    @Autowired
    private NfsPathConfig nfsPathConfig;
    @Override
    protected String doAction(DialProcessSession session, BaseActionData actionData) throws ProcessActionException {
        String callId = session.getCallId();
        String callUuid = session.getCallUuid();
        session.setStatus(ProcessSessionStatusEnum.PLAYING);
        PlayFileActionData playFileAction = (PlayFileActionData) actionData;
        session.setEnableInterrupt(YesNoEnum.YES.getCode().equals(playFileAction.getEnableInterrupt())) ;
        log.info("callId:{},callUuid:{},content:{},play file:{}", callId, callUuid,playFileAction.getContent(), playFileAction.getFilePath());
        File file = new File(nfsPathConfig.getBase() + playFileAction.getFilePath());
//        if (!file.exists()) {
//            throw new ProcessActionException("播音文件不存在");
//        }
        getFsApiCommand().playFile( callUuid,nfsPathConfig.getBase() + playFileAction.getFilePath());
        return playFileAction.getContent();
    }

    @Override
    public ProcessResActionEnum getActionEnum() {
        return ProcessResActionEnum.PLAY_FILE;
    }
}
