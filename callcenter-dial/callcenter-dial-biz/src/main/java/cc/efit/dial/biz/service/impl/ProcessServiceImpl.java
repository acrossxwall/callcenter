package cc.efit.dial.biz.service.impl;

import cc.efit.dial.biz.service.ProcessService;
import cc.efit.process.api.ProcessServiceGrpc;
import cc.efit.process.api.ProcessServiceProto;
import cc.efit.process.api.action.*;
import cc.efit.process.api.enums.ProcessResActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {
    @GrpcClient("process-service")
    private ProcessServiceGrpc.ProcessServiceBlockingStub processServiceBlockingStub;

    @Override
    public ChatProcessRes requestProcessChat( ChatProcessReq chatReq) {
        log.info("开始请求process service:{}", chatReq);
        //此处封装下，尽量把grpc 的协议的代码侵入降到最低
        try {
            ProcessServiceProto.ChatProcessReq.Builder builder = ProcessServiceProto.ChatProcessReq.newBuilder();
            builder.setCallId(chatReq.callId());
            builder.setCallTemplateId(chatReq.callTemplateId());
            builder.setAction(chatReq.action());
            builder.setContent(chatReq.content());
            if (chatReq.customerInfo()!=null && !chatReq.customerInfo().isEmpty()) {
                builder.putAllCustomerInfo(chatReq.customerInfo());
            }
            ProcessServiceProto.ChatProcessRes  res = processServiceBlockingStub.chatProcess(builder.build());
            log.info("请求process service成功:{}", res);
            ChatProcessRes chatRes = new ChatProcessRes();
            chatRes.setCallId(res.getCallId());
            chatRes.setMatchResult(buildMatchResult(res.getMatchResult()));
            chatRes.setFlowData(buildFlowData(res.getFlowData()));
            chatRes.setActions(buildActions(res.getActionsList()));
            return chatRes;
        }catch (Exception e){
            //抛出异常，上层处理可能需要挂机，因为流程处理失败了
            log.error("请求process service失败",  e);
            throw e;
        }
    }

    private List<BaseActionData> buildActions(List<ProcessServiceProto.ActionData> actionsList) {
        if (actionsList==null || actionsList.isEmpty()) {
            return null;
        }
        return actionsList.stream().map(this::buildAction).toList();
    }

    private BaseActionData buildAction(ProcessServiceProto.ActionData actionData) {
        ProcessResActionEnum actionEnum = ProcessResActionEnum.getEnumByAction(actionData.getAction());
        assert actionEnum != null;
        return switch (actionEnum ){
            case  CREATE_CHANNEL  -> new CreateChannelActionData();
            case  PLAY_FILE -> {
                PlayFileActionData playFileActionData = new PlayFileActionData();
                playFileActionData.setFilePath(actionData.getPlayFile().getFilePath());
                playFileActionData.setEnableInterrupt(actionData.getPlayFile().getEnableInterrupt());
                playFileActionData.setContent(actionData.getPlayFile().getContent());
                yield playFileActionData;
            }
            case PLAY_TTS -> {
                PlayTtsActionData playTtsActionData = new PlayTtsActionData();
                playTtsActionData.setContent(actionData.getPlayTts().getContent());
                playTtsActionData.setEnableInterrupt(actionData.getPlayTts().getEnableInterrupt());
                yield playTtsActionData;
            }
            case TRANSFER_AGENT -> {
                TransferActionData transferActionData = new TransferActionData();
                transferActionData.setType(actionData.getTransfer().getType());
                transferActionData.setNumber(actionData.getTransfer().getNumber());
                transferActionData.setAgentGroupId(actionData.getTransfer().getAgentGroupId());
                yield transferActionData;
            }
            case ProcessResActionEnum.HANGUP -> {
                HangupActionData hangupActionData = new HangupActionData();
                hangupActionData.setReason(actionData.getHangup().getReason());
                yield hangupActionData;
            }
        };
    }

    private DialogueFlowData buildFlowData(ProcessServiceProto.DialogueFlowData flowData) {
        if (flowData==null) {
            return null;
        }
        return new DialogueFlowData(flowData.getNodeId(),
                flowData.getNodeName(), flowData.getNodeLabel(), flowData.getLevelName());
    }

    private MatchResult buildMatchResult(ProcessServiceProto.MatchResult matchResult) {
        if (matchResult==null) {
            return null;
        }
        return new MatchResult(matchResult.getFlowId(), matchResult.getKeyword(),
                matchResult.getIntentionId(), matchResult.getIntentionName(), matchResult.getKeywordMatch(),
                matchResult.getNluMatch(), matchResult.getNluMatchCode());
    }
}
