package cc.efit.process.api.action;

import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.Getter;

/**
 * @author across
 * @Description
 * @Date 2025-08-30 10:01
 */

public abstract sealed class BaseActionData permits CreateChannelActionData,
        PlayFileActionData, PlayTtsActionData,HangupActionData,TransferActionData {
    @Getter
    private final ProcessResActionEnum action;

    protected  BaseActionData(ProcessResActionEnum action){
        this.action = action;
    }
}
