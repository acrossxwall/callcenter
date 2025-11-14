package cc.efit.call.api.enums;

public class CallTaskEnum {
    public enum TaskStatus {
        DISABLE(0, "禁用"),
        ENABLE(1, "启用");
        private Integer status;
        private String desc;
        TaskStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {
            return status;
        }
    }

    public enum TaskCallStatus  {
//        0-未启动，1-运行中，2-暂停中 3-已暂停 4-已完成
        NOT_START(0, "未开始"),
        RUNNING(1, "进行中"),
        PAUSE(2, "暂停中"),
        PAUSED(3, "已暂停"),
        FINISHED(4, "已完成"),
        ;
        private Integer status;
        private String desc;
        TaskCallStatus(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {
            return status;
        }
    }
}
