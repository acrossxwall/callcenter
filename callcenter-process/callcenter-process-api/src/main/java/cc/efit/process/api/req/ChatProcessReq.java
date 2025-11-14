package cc.efit.process.api.req;

import java.util.Map;

public record ChatProcessReq(
        /**
         * 请求动作
         * 参考 ProcessReqActionEnum
         */
      Integer action,
        /**
         *  客户说话内容 
         */
      String content,
        /**
         * 通话唯一标识
         */
      String callId,
        /**
         * 模板id
         */
      Integer callTemplateId,
      /**
       * 客户信息
       */
      Map<String,String> customerInfo
){}
