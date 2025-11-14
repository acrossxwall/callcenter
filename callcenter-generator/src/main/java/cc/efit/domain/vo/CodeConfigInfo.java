package cc.efit.domain.vo;

import cc.efit.domain.CodeConfig;
import cc.efit.domain.ColumnInfo;

import java.util.List;

public record CodeConfigInfo (CodeConfig info, List<TableInfo> tables, List<ColumnInfo> rows) {

}
