package cc.efit.generator.domain.vo;

import cc.efit.generator.domain.CodeConfig;
import cc.efit.generator.domain.ColumnInfo;

import java.util.List;

public record CodeConfigInfo (CodeConfig info, List<TableInfo> tables, List<ColumnInfo> rows) {

}
