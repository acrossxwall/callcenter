package cc.efit.dial.api.req;

import cc.efit.process.api.res.TemplateGlobalSettingRes;

public record DialPhoneReq(
        /**
         * 线路信息
         */
        LineInfoReq lineInfo,
        /**
         * 客户信息
         */
        CallCustomerInfoReq customer,
        /**
         * 模板设置信息，传过来节省查询时间
         */
        TemplateGlobalSettingRes settings
) {
}
