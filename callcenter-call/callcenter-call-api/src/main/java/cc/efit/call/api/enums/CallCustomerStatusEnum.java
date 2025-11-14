package cc.efit.call.api.enums;

public class CallCustomerStatusEnum {

    public enum CustomerStatus {
        NOT_CALL(0, "未呼叫"),
        LOADING(1,"已加载"),
        CALLING(2, "进行中"),
        FINISH(3, "已完成");
        private Integer status;
        private String desc;
        CustomerStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {
            return status;
        }
    }

    public enum CustomerCallStatus {
        REJECT(0,"未接听"),
        CONNECT(1,"已接听"),
        BLACK(2,"黑名单"),
        CALL_LIMIT(3,"呼叫限制"),
        CONNECT_LIMIT(4,"接听限制"),
        ;
        private Integer status;
        private String desc;
        CustomerCallStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {
            return status;
        }
    }
}
