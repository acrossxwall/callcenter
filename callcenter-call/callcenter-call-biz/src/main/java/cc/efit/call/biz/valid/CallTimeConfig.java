package cc.efit.call.biz.valid;

import lombok.Data;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
@Data
public class CallTimeConfig  {
    /**
     * 允许的星期几，7-6，7表示星期天
     */
    private Set<Integer> allowDays;
    /**
     * 允许的时间范围
     */
    private TimeRange allowTimeRange;
    /**
     * 禁止的时间范围
     */
    private TimeRange denyTimeRange;

    public CallTimeConfig(String allowDayStr, String allowTime, String denyTime) {
        this.allowDays = parseAllowDays(allowDayStr);
        this.allowTimeRange = new TimeRange(allowTime);
        this.denyTimeRange = new TimeRange(denyTime);
    }

    private Set<Integer> parseAllowDays(String allowDayStr) {
        return Arrays.stream(allowDayStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}