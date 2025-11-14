package cc.efit.process.biz.handler.utils;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.HangupActionData;
import cc.efit.process.api.action.TransferActionData;

public class TemplateCommonUtils {

    public static BaseActionData buildHangupAction(String reason) {
        HangupActionData data = new HangupActionData();
        data.setReason(reason);
        return data;
    }

    public static BaseActionData  buildTransferAction(String transferNumber, Integer transferType,
                                                      Integer transferAgentGroupId) {
        TransferActionData data = new TransferActionData();
        data.setNumber(transferNumber);
        data.setType(transferType);
        data.setAgentGroupId(transferAgentGroupId);
        return data;
    }
}
