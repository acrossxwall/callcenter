package cc.efit.dial.biz.client;

import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.api.req.DialPhoneReq;
import cc.efit.dial.api.req.LineInfoReq;
import cc.efit.esl.core.FsClient;
import cc.efit.esl.core.message.SendMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class FsApiCommand {
    @Autowired
    private FsClient fsClient;

    public void callPhone(LineInfoReq lineInfo, CallCustomerInfoReq customer) {
        callPhone(true,lineInfo, customer);
    }

    public void callPhone(boolean async,LineInfoReq lineInfo, CallCustomerInfoReq customer) {
        String callId = customer.callId();
        //命令不应该换行
        String format = "{absolute_codec_string=^^:pcmu:pcma,fire_asr_events=true,origination_caller_id_number=%s,callId=%s}%s/%s 9999 ";
        String phoneNumber = customer.phone();
        if (lineInfo.callPrefix()!=null) {
            phoneNumber = lineInfo.callPrefix() + phoneNumber;
        }
        String args ;
        if (lineInfo.gateway()) {
            args = format.formatted(lineInfo.callNumber(),callId, "sofia/gateway/"+ lineInfo.gatewayName(), phoneNumber);
        }else{
            args = format.formatted(lineInfo.callNumber(),callId, "user" , phoneNumber);
        }
        log.info("拨打电话callId:{},构建参数是 originate {}",callId, args);
        if (async) {
            fsClient.sendBackgroundApiCommand("originate", args);
        }else{
            fsClient.sendApiCommand("originate", args);
        }
    }

    public void playFile(String uuid, String filePath) {
        playFile(uuid,"aleg", filePath);
    }

    public void playFile(String uuid,String leg,String filePath) {
        String args = "%s %s %s".formatted(uuid, filePath,leg);
        log.info("播放文件uuid:{},播放文件路径是 {}, leg:{}",uuid, filePath, leg);
        fsClient.sendApiCommand("uuid_broadcast", args);
    }

    public void hangupChannel(String uuid ) {
        log.info("挂断电话uuid:{} ",uuid );
        fsClient.sendApiCommand("uuid_kill", uuid);
    }

    public void pausePlayChannel(String uuid ) {
        log.info("暂停播音电话uuid:{} ",uuid );
        fsClient.sendApiCommand("uuid_break", uuid);
    }

    public void startVoskAsr(String uuid) {
        sendCommandMsg(uuid,"detect_speech", "vosk default default");
    }

    public void sendCommandMsg(String uuid,  String appName,String args) {
        sendCommandMsg(uuid,"execute", appName,args);
    }

    public void sendCommandMsg(String uuid, String command,String appName,String args) {
        if (command==null || appName==null) {
            return;
        }
        SendMsg msg = new SendMsg(uuid );
        msg.addCallCommand(command);
        msg.addExecuteAppName(appName);
        if (args!=null) {
            msg.addExecuteAppArg(args);
        }
        log.info("发送命令uuid:{},命令是 {}, appName: {}, args:{},构建后:{}",uuid, command, appName, args,msg);
        fsClient.sendMessage(  msg);
    }
}
