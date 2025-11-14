package cc.efit.call.biz.vo.line;

import java.util.List;

public record AssignLineVo(Integer id,Integer concurrency, List<AssignLineItemInfo> assignInfo) {
}
