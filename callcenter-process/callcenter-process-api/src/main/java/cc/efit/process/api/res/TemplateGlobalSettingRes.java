package cc.efit.process.api.res;

public record TemplateGlobalSettingRes (
        /**
         * 允许打断
         */
        boolean enableInterrupt,
        /**
         * 前 多少秒不能打断
         */
        Integer interruptSeconds,
        /**
         * 启用静音无应答配置
         */
        boolean enableNoReply,
        /**
         * 无应答等待时长
         */
        Integer noReplySeconds
){
}
