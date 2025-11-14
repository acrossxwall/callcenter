package cc.efit.dial.api.req;

import java.util.Map;

public record CallCustomerInfoReq(
        /**
         * 客户id
         */
        Integer id,
        /**
         * 客户电话
         */
        String phone,
        /**
         * 任务id
         */
        Integer taskId,
        /**
         * 任务名称
         */
        String taskName,
        /**
         * 呼叫id
         */
        String callId,
        /**
         * 客户信息
         */
        Map<String,String> customerInfo,
        /**
         * 模板id
         */
        Integer callTemplateId,
        /**
         * 模板名称
         */
        String callTemplateName
) {
}
