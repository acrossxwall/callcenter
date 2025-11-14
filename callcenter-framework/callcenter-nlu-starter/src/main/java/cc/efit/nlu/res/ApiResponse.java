package cc.efit.nlu.res;

import lombok.Data;


@Data
public class ApiResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return code != null && code == 0;
    }

}