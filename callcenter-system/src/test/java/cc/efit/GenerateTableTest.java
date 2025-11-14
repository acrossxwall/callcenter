package cc.efit;

import cc.efit.domain.ColumnInfo;
import cc.efit.domain.CodeConfig;
import cc.efit.service.CodeConfigService;
import cc.efit.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AppRun.class)
public class GenerateTableTest {
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private CodeConfigService genConfigService;

    @Test
    public void testFindTableColumn() {
        List<ColumnInfo> list = generatorService.query("sys_user");
        System.out.println(list);
    }

    @Test
    public void testGenerateCode() {
        String tableName = "sys_user";
        generatorService.generator(tableName);
    }
}
