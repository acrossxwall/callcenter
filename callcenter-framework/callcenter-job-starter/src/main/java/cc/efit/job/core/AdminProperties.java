package cc.efit.job.core;

import lombok.Data;

@Data
public class AdminProperties {
    private String addresses;

    private String accessToken;

    private int timeout;
}
