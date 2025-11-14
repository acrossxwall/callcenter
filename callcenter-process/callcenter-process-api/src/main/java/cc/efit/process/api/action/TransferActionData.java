package cc.efit.process.api.action;

import cc.efit.process.api.enums.ProcessResActionEnum;
import cc.efit.json.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class TransferActionData extends BaseActionData {
    public TransferActionData( ) {
        super(ProcessResActionEnum.TRANSFER_AGENT);
    }
    //TODO 如果是号码的的最好还有网关，这样可以直接转到第三方
    /** 转接坐席的手机号码 */
    private String number;

    /** 转接方式 电话号码1 坐席组2 */
    private Integer type;
    /** 转接坐席组 */
    private Integer agentGroupId;
    public String toString(){
        return JsonUtils.toJsonString(this);
    }
}
