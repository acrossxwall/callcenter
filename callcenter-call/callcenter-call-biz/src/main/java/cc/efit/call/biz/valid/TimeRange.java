package cc.efit.call.biz.valid;

import lombok.Data;

import java.time.LocalTime;
@Data
public class TimeRange{
    /**
     * 开始时间节点
     */
    private LocalTime startTime;
    /**
     * 结束时间节点
     */
    private LocalTime endTime;
    public TimeRange(String timeRangeStr) {
        String[] times = timeRangeStr.split("-");
        this.startTime = LocalTime.parse(times[0]);
        this.endTime = LocalTime.parse(times[1]);
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    public boolean validTimeRange() {
        return startTime.isAfter(endTime);
    }
}