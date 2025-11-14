package cc.efit.process.biz.api;


import cc.efit.process.api.ChatProcessVoipApi;
import cc.efit.process.api.ProcessServiceGrpc;
import cc.efit.process.api.ProcessServiceProto;
import cc.efit.process.api.action.*;
import cc.efit.process.api.enums.ProcessResActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author across
 * @Description
 * @Date 2025-09-12 20:42
 */
@GrpcService
public class ChatProcessProcServiceImpl extends ProcessServiceGrpc.ProcessServiceImplBase {
    @Autowired
    private ChatProcessVoipApi chatProcessVoipApi;

    @Override
    public void chatProcess(ProcessServiceProto.ChatProcessReq request, StreamObserver<ProcessServiceProto.ChatProcessRes> responseObserver) {
        ChatProcessReq req = new ChatProcessReq(request.getAction(),
                request.getContent(), request.getCallId(), request.getCallTemplateId(),
                request.getCustomerInfoMap());
        ChatProcessRes res = chatProcessVoipApi.chatProcess(req);
        if (res==null) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
            return;
        }
        ProcessServiceProto.DialogueFlowData flowData = buildProcFlowData(res.getFlowData());
        ProcessServiceProto.MatchResult matchResult = buildProcMatchResult(res.getMatchResult());
        List<ProcessServiceProto.ActionData> actionDataList = buildProcActionData(res.getActions());
        ProcessServiceProto.ChatProcessRes response = ProcessServiceProto.ChatProcessRes.newBuilder()
                .setCallId(res.getCallId())
                .addAllActions(actionDataList)
                .setFlowData(flowData)
                .setMatchResult(matchResult)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ProcessServiceProto.DialogueFlowData buildProcFlowData(DialogueFlowData javaFlowData) {
        ProcessServiceProto.DialogueFlowData.Builder builder =  ProcessServiceProto.DialogueFlowData.newBuilder();
        if (javaFlowData !=null) {
            builder
                    .setNodeId(javaFlowData.nodeId())
                    .setNodeName(javaFlowData.nodeName())
                    .setNodeLabel(javaFlowData.nodeLabel())
                    .setLevelName(javaFlowData.levelName())
                    .build();
        }
        return builder.build();
    }

    private ProcessServiceProto.MatchResult buildProcMatchResult(MatchResult javaMatchResult) {
        ProcessServiceProto.MatchResult.Builder builder = ProcessServiceProto.MatchResult.newBuilder();
        if (javaMatchResult !=null) {
            builder
                    .setFlowId(javaMatchResult.flowId())
                    .setKeyword(javaMatchResult.keyword())
                    .setIntentionId(javaMatchResult.intentionId())
                    .setIntentionName(javaMatchResult.intentionName())
                    .setKeywordMatch(javaMatchResult.keywordMatch())
                    .setNluMatchCode(javaMatchResult.nluMatchCode())
                    .build();
        }
        return builder.build();
    }

    private List<ProcessServiceProto.ActionData> buildProcActionData(List<BaseActionData> javaActionDataList) {
        List<ProcessServiceProto.ActionData> actionDataList = new ArrayList<>();
        if (javaActionDataList !=null) {
            actionDataList = javaActionDataList.stream().map(javaActionData -> {
                ProcessResActionEnum actionEnum = javaActionData.getAction();
                ProcessServiceProto.ActionData.Builder builder =  ProcessServiceProto.ActionData.newBuilder() ;
                builder.setAction(actionEnum.getAction());
                switch (actionEnum) {
                    case CREATE_CHANNEL -> builder.setCreateChannel(ProcessServiceProto.CreateChannelAction.newBuilder().setAction(actionEnum.getAction()).build());
                    case PLAY_FILE ->  {
                        PlayFileActionData playFileActionData = (PlayFileActionData) javaActionData;
                        builder.setPlayFile(ProcessServiceProto.PlayFileAction.newBuilder().setAction(actionEnum.getAction()).
                                setContent(playFileActionData.getContent())
                                .setEnableInterrupt(playFileActionData.getEnableInterrupt())
                                .setFilePath(playFileActionData.getFilePath()).build());
                    }
                    case PLAY_TTS -> {
                        PlayTtsActionData playTtsActionData = (PlayTtsActionData) javaActionData;
                        builder.setPlayTts(ProcessServiceProto.PlayTtsAction.newBuilder().setAction(actionEnum.getAction())
                                .setContent(playTtsActionData.getContent())
                                .setEnableInterrupt(playTtsActionData.getEnableInterrupt()).build());
                    }
                    case TRANSFER_AGENT -> {
                        TransferActionData transferActionData = (TransferActionData) javaActionData;
                        builder.setTransfer(ProcessServiceProto.TransferAction.newBuilder().setAction(actionEnum.getAction())
                                .setNumber(transferActionData.getNumber())
                                .setAgentGroupId(transferActionData.getAgentGroupId())
                                .setType(transferActionData.getType())
                                .build());
                    }
                    case HANGUP -> {
                        HangupActionData hangupActionData = (HangupActionData) javaActionData;
                        builder.setHangup(ProcessServiceProto.HangupAction.newBuilder().setAction(actionEnum.getAction())
                                .setReason(hangupActionData.getReason()).build());
                    }
                }
                return builder.build();
            }).toList();
        }
        return actionDataList;
    }
}
