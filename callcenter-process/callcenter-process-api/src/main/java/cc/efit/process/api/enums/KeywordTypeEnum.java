package cc.efit.process.api.enums;

public enum KeywordTypeEnum {
    TEXT(0), //普通关键词
    REGEX(1), //正则表达式
    EXPRESSION(2), //逻辑表达式
    ;
    private final Integer type;
    KeywordTypeEnum(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }
}
