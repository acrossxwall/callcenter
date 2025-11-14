package cc.efit.call.biz.valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Service
@Slf4j
public class CallValidationServiceImpl implements CallValidationService {
    @Override
    public boolean validateCallTime(LocalDateTime callTime, CallTimeConfig config) {
        log.info("校验外呼时间是否允许外呼callTime: {}, config: {}", callTime, config);
        // 1. 校验星期几
        if (!isAllowedDay(callTime, config.getAllowDays())) {
            return false;
        }
        LocalTime currentTime = callTime.toLocalTime();
        // 2. 校验是否在禁止时间段
        if (config.getDenyTimeRange() != null &&
                config.getDenyTimeRange().contains(currentTime)) {
            return false;
        }
        // 3. 校验是否在允许时间段
        boolean result = config.getAllowTimeRange()==null ||  config.getAllowTimeRange().contains(currentTime);
        log.info("校验外呼时间是否允许外呼结果result: {}", result);
        return result;
    }

    /**
     * 校验星期几是否允许
     */
    private boolean isAllowedDay(LocalDateTime dateTime, Set<Integer> allowDays) {
        if (allowDays == null || allowDays.isEmpty()) {
            return false;
        }

        int dayOfWeek = convertToCustomDayOfWeek(dateTime.getDayOfWeek());
        return allowDays.contains(dayOfWeek);
    }

    /**
     * 转换星期格式: Java的DayOfWeek(1=周一,7=周日) -> 自定义格式(7=周日,1=周一,...,6=周六)
     */
    private int convertToCustomDayOfWeek(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }
}
