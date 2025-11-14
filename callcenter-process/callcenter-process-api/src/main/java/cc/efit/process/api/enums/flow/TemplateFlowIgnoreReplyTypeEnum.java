package cc.efit.process.api.enums.flow;

public enum TemplateFlowIgnoreReplyTypeEnum {
//    用户回复0:忽略用户回复 1:忽略用户除拒绝外的所有回复 2:都不忽略
    IGNORE_REPLY(0),
    IGNORE_REPLY_EXCEPT_REFUSE(1),
    IGNORE_NOTHING(2);
    private Integer type;
    TemplateFlowIgnoreReplyTypeEnum(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }
}
