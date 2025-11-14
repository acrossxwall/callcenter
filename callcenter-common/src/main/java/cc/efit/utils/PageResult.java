package cc.efit.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 *
 * @date 2018-11-23
 * @param <T>
 */
public record PageResult<T> (List<T> rows,long total) implements Serializable {

}
