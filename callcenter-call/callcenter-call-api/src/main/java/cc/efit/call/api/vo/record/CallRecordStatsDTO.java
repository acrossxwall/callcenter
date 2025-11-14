package cc.efit.call.api.vo.record;

public record CallRecordStatsDTO(
      Integer taskId,
      /**
       * 总呼叫数量
       */
      Long totalCalls,
      /**
       * 接听数量
       */
      Long answeredCalls,
      /**
       * 通话总时长单位s
       */
      Long totalDuration)
{}