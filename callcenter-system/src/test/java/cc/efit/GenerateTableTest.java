package cc.efit;

import cc.efit.generator.domain.ColumnInfo;
import cc.efit.generator.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AppRun.class)
public class GenerateTableTest {
    @Autowired
    private GeneratorService generatorService;

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
