package cc.efit.process.api.enums.flow;

public enum TemplateVerbalTypeEnum {

    /** 0-顺序播报 1-随机播报 */
    ORDER(0), RANDOM(1);
    private Integer code;
    TemplateVerbalTypeEnum(Integer code) {
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }
}
