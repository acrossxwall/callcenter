package cc.efit.call.biz.vo.customer;

import cn.hutool.poi.excel.sax.handler.RowHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelCustomerRowHandler implements RowHandler {
    private List<Map<String,String>> result;
    private Map<Integer, String> header;
    public ExcelCustomerRowHandler() {
        result = new ArrayList<>();
        header = new HashMap<>();
    }
    @Override
    public void handle(int sheetIndex, long rowIndex, List<Object> rowCells) {
        log.info("sheetIndex:{},rowIndex:{},rowCells:{}",sheetIndex,rowIndex,rowCells);
        if (rowIndex==0) {
            //这是header行，处理header 不处理
            for (int i = 0; i < rowCells.size(); i++) {
                Object currVal = rowCells.get(i);
                if (currVal==null || currVal.toString().isEmpty()) {
                    continue;
                }
                header.put(i, (String) currVal);
            }
            return;
        }
        Map<String,String> row = new HashMap<>();
        for (int i = 0; i < rowCells.size(); i++) {
            Object currVal = rowCells.get(i);
            String currentHeader = header.get(i);
            if (currentHeader==null ) {
                continue;
            }
            row.put(currentHeader, currVal==null?"":currVal.toString());
        }
        result.add(row);
    }

    public List<Map<String,String>> getResult() {
        return result;
    }
}
