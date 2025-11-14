package cc.efit.call.biz.vo.customer;

import java.util.List;

public record ImportCustomerInfo(
        Integer taskId,
        Integer fileId,
        Integer importType,
        List<CustomerItemInfo> customers
) {
}
