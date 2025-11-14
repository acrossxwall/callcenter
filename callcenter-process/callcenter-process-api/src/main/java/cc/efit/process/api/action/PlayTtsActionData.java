package cc.efit.process.api.action;

import cc.efit.process.api.enums.ProcessResActionEnum;
import cc.efit.json.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author across
 * @Description
 * @Date 2025-08-31 11:06
 */
@Getter
@Setter
public final class PlayTtsActionData extends BaseActionData {
    public PlayTtsActionData( ) {
        super(ProcessResActionEnum.PLAY_TTS);
    }
    /** 允许被打断 1:是 0:否 */
    private Integer enableInterrupt;
    private String content;
    public String toString(){
        return JsonUtils.toJsonString(this);
    }
}
