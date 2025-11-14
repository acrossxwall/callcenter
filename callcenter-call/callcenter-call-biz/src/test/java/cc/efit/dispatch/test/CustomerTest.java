package cc.efit.dispatch.test;

import cc.efit.call.biz.vo.customer.ExcelCustomerRowHandler;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    public  void testCustomerImport() {
        ExcelCustomerRowHandler excelCustomerRowHandler = new ExcelCustomerRowHandler();
        String filePath = "E:\\外呼名单模板.xls.xlsx";
        ExcelUtil.readBySax(filePath, 0, excelCustomerRowHandler);
        System.out.println(excelCustomerRowHandler.getResult());
    }
}
