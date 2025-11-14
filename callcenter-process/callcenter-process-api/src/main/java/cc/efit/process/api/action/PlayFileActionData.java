package cc.efit.process.api.action;

import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author across
 * @Description
 * @Date 2025-08-31 11:00
 */
@Setter
@Getter
public final class PlayFileActionData extends BaseActionData{
    public PlayFileActionData( ) {
        super(ProcessResActionEnum.PLAY_FILE);
    }
    private String filePath;
    /** 允许被打断 1:是 0:否 */
    private Integer enableInterrupt;
    private String content;
    public String toString(){
        return JsonUtils.toJsonString(this);
    }
}
