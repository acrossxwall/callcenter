package cc.efit.dial.api.enums;

public enum InteractiveRecordRoleEnum {
    AI(1,"AI"),
    USER(2,"用户"),
    AGENT(3,"坐席"),
    ;
    int role;
    String desc;
    InteractiveRecordRoleEnum(int role, String desc) {
        this.role = role;
        this.desc = desc;
    }

    public int getRole() {
        return role;
    }

}
