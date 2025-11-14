package cc.efit.process.api.action;

import cc.efit.process.api.enums.ProcessResActionEnum;
import cc.efit.json.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HangupActionData extends BaseActionData {
    /**
     * 挂机原因
     */
    private String reason;
    public HangupActionData( ) {
        super(ProcessResActionEnum.HANGUP);
    }
    public String toString(){
        return JsonUtils.toJsonString(this);
    }
}
