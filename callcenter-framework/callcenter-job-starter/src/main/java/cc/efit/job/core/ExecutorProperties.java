package cc.efit.job.core;

import lombok.Data;

@Data
public class ExecutorProperties {
    private String appname;

    private String address;

    private String ip;

    private int port;

    private String logPath;

    private int logRetentionDays;

    private int jobGroup;

    private String alertEmail;
}
