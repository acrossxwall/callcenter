/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cc.efit.service.impl;

import cc.efit.core.constants.CommonConstants;
import cc.efit.constants.GenConstants;
import cc.efit.domain.vo.CodeConfigInfo;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.exception.ServiceException;
import cc.efit.repository.CodeConfigRepository;
import cc.efit.res.R;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.RequiredArgsConstructor;
import cc.efit.domain.CodeConfig;
import cc.efit.domain.ColumnInfo;
import cc.efit.domain.vo.TableInfo;
import cc.efit.exception.BadRequestException;
import cc.efit.repository.ColumnInfoRepository;
import cc.efit.service.GeneratorService;
import cc.efit.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @date 2019-01-02
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"unchecked","all"})
public class GeneratorServiceImpl implements GeneratorService {
    private static final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);
    @PersistenceContext
    private EntityManager em;

    private final ColumnInfoRepository columnInfoRepository;

    private final CodeConfigRepository codeConfigRepository;

    private final String CONFIG_MESSAGE = "请先配置生成器";

    @Override
    public PageResult<TableInfo> getTables(String name, int[] startEnd) {
        // 使用预编译防止sql注入
        List<TableInfo> tableInfos = getTableInfos(name, startEnd,true, false);
        String countSql = """
                        select count(1) from information_schema.tables  
                        where table_schema = 'public' and table_name like :table
                        """;
        Query queryCount = em.createNativeQuery(countSql);
        queryCount.setParameter("table", StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        long totalElements = (long) queryCount.getSingleResult();
        return PageUtil.toPage(tableInfos, totalElements );
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfos = columnInfoRepository.findByTableNameOrderByIdAsc(tableName);
        if (CollectionUtil.isNotEmpty(columnInfos)) {
            return columnInfos;
        } else {
            columnInfos = query(tableName);
            return columnInfoRepository.saveAll(columnInfos);
        }
    }

    @Override
    public List<ColumnInfo> query(String tableName) {
        // 使用预编译防止sql注入
        String sql = """
                select column_name, is_nullable, udt_name ,\s
                col_description((table_schema || '.' || table_name)::regclass::oid, ordinal_position) AS column_comment ,
                CASE\s
                        WHEN (SELECT COUNT(*) FROM information_schema.key_column_usage\s
                              WHERE table_schema = c.table_schema AND table_name = c.table_name\s
                              AND column_name = c.column_name) > 0 THEN 'PRI'\s
                        ELSE ''\s
                    END AS column_key  \s
                from information_schema.columns c\s
                                 where table_name = ? and table_schema = 'public' order by ordinal_position
                """;
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, tableName);
        List result = query.getResultList();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            columnInfos.add(
                    new ColumnInfo(
                            tableName,
                            "NO".equals(arr[1]),
                            arr[0].toString(),
                            arr[2].toString(),
                            ObjectUtil.isNotNull(arr[4]) ? arr[4].toString() : null,
                            ObjectUtil.isNotNull(arr[3]) ? arr[3].toString() : null
                ));
        }
        return columnInfos;
    }

    @Override
    @Transactional
    public void sync(List<String> tables,boolean force) {
        try  {
            int[] startEnd = new int[]{0,1};
            for (String tableName : tables) {
                CodeConfig codeConfig =  codeConfigRepository.findByTableName(tableName);
                if (codeConfig!=null) {
                    //说明已经存在配置
                    if (force) {
                        //强制更新
                        codeConfigRepository.delete(codeConfig);
                        columnInfoRepository.deleteByTableName(tableName);
                    }else{
                        throw new ServiceException("表"+tableName+"已经存在配置，请使用强制更新");
                    }
                }
                List<TableInfo> tableInfos = getTableInfos(tableName, startEnd,true,true);
                if (tableInfos==null || tableInfos.isEmpty()) {
                    continue;
                }
                TableInfo tableInfo = tableInfos.getFirst();
                CodeConfig config = GenUtil.initTable(tableInfo );
                codeConfigRepository.save(config);
                    // 保存列信息
                List<ColumnInfo> genTableColumns = query(tableName);
                for (ColumnInfo column : genTableColumns) {
                    column.setTableId(config.getId());
                    GenUtil.initColumnField(column, config);
                }
                columnInfoRepository.saveAll(genTableColumns);
            }
        } catch (Exception e) {
            log.error("同步失败", e);
            throw new ServiceException("同步失败"  );
        }
    }

    @Override
    public void generator(String tableName) {
        CodeConfig table = codeConfigRepository.findByTableName(tableName);
        if (table==null || table.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        List<ColumnInfo> columns = columnInfoRepository.findByTableNameOrderByIdAsc(tableName );
        table.setColumns(columns);
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);
        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)  {
                // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CommonConstants.CHARSET);
            tpl.merge(context, sw);
            try  {
                String path = getGenPath(table, template);
                FileUtils.writeStringToFile(new File(path), sw.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ServiceException("渲染模板失败，表名：" + table.getTableName());
            }
        }
    }

    @Override
    public R preview(CodeConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        List<Map<String, Object>> genList = GenUtil.preview(columns, genConfig);
        return R.ok(genList);
    }

    @Override
    public void download(CodeConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        try {
            File file = new File(GenUtil.download(columns, genConfig));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtil.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new BadRequestException("打包失败");
        }
    }

    @Override
    public CodeConfigInfo findCodeConfigInfo(Integer id) {
        CodeConfig info = codeConfigRepository.findById(id).orElseGet(CodeConfig::new);
        List<TableInfo> tables = getTableInfos(null, null,false,false);
        List<ColumnInfo> columns = columnInfoRepository.findByTableNameOrderByIdAsc(info.getTableName());
        return new CodeConfigInfo(info, tables, columns);
    }

    private List<TableInfo> getTableInfos(String name, int[] startEnd,boolean page, boolean equal) {
        String sql = """
                SELECT\s
                    table_name,\s
                    obj_description(c.oid, 'pg_class') as table_comment
                FROM\s
                    information_schema.tables t
                JOIN\s
                    pg_catalog.pg_class c ON (t.table_name = c.relname )
                WHERE\s
                    t.table_schema='public' 
                """;
        if (equal) {
            sql += " and t.table_name = :table";
        }else{
            sql += " and t.table_name like :table";
        }
        Query query = em.createNativeQuery(sql);
        if (page) {
            query.setFirstResult(startEnd[0]);
            query.setMaxResults(startEnd[1] - startEnd[0]);
        }
        if (equal) {
            query.setParameter("table", name);
        }else{
            query.setParameter("table", StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        }
        List result = query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            if (arr[0]==null) {
                continue;
            }
            tableInfos.add(new TableInfo(arr[0].toString(), arr[1]==null?"":arr[1].toString()));
        }
        return tableInfos;
    }

    public void setSubTable(CodeConfig table)  {
        String subTableName = table.getSubTableName();
        if (StringUtils.isNotEmpty(subTableName)){
            table.setSubTable(codeConfigRepository.findByTableName(subTableName));
        }
    }

    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     */
    public void setPkColumn(CodeConfig table) {
        for (ColumnInfo column : table.getColumns()) {
            if (YesNoEnum.YES.getCode().equals(column.getIsPk())) {
                table.setPkColumn(column);
                break;
            }
        }
        if (  table.getPkColumn()==null)
        {
            table.setPkColumn(table.getColumns().getFirst());
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
            for (ColumnInfo column : table.getSubTable().getColumns())  {
                if (YesNoEnum.YES.getCode().equals(column.getIsPk())) {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if ( table.getSubTable().getPkColumn()==null ) {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().getFirst());
            }
        }
    }

    /**
     * 获取代码生成地址
     *
     * @param table 业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getGenPath(CodeConfig table, String template) {
        String genPath = table.getGenPath();
        if (Objects.equals(genPath, "/"))  {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }
}
