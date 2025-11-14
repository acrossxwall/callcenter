package cc.efit.utils;

import java.util.*;

import cc.efit.constants.GenConstants;
import cc.efit.domain.CodeConfig;
import cc.efit.domain.ColumnInfo;
import cc.efit.domain.vo.CodeConfigOptions;
import cc.efit.json.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

/**
 * 模板处理工具类
 * 
 * @author ruoyi
 */
public class VelocityUtils
{
    /** 项目空间路径 */
    private static final String PROJECT_PATH = "main/java";

    /** 默认上级菜单，系统工具 */
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(CodeConfig genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtil.getToday());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable));
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        velocityContext.put("dicts", getDicts(genTable));
        setMenuVelocityContext(velocityContext, genTable);
        if (GenConstants.TPL_TREE.equals(tplCategory))
        {
            setTreeVelocityContext(velocityContext, genTable);
        }
        if (GenConstants.TPL_SUB.equals(tplCategory))
        {
            setSubVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    public static void setMenuVelocityContext(VelocityContext context, CodeConfig genTable)
    {
        String options = genTable.getOptions();
        CodeConfigOptions paramsObj = JsonUtils.parseObject(options, CodeConfigOptions.class);
        assert paramsObj != null;
        String parentMenuId = getParentMenuId(paramsObj);
        context.put("parentMenuId", parentMenuId);
    }

    public static void setTreeVelocityContext(VelocityContext context, CodeConfig genTable)
    {
        String options = genTable.getOptions();
        CodeConfigOptions paramsObj = JsonUtils.parseObject(options, CodeConfigOptions.class);
        assert paramsObj != null;
        String treeCode = getTreecode(paramsObj);
        String treeParentCode = getTreeParentCode(paramsObj);
        String treeName = getTreeName(paramsObj);

        context.put("treeCode", treeCode);
        context.put("treeParentCode", treeParentCode);
        context.put("treeName", treeName);
        context.put("expandColumn", getExpandColumn(genTable));
        if (StringUtils.isNotBlank(paramsObj.treeParentCode()) ) {
            context.put("tree_parent_code",paramsObj.treeParentCode());
        }
        if (StringUtils.isNotBlank(paramsObj.treeName())) {
            context.put("tree_name", paramsObj.treeName());
        }
    }

    public static void setSubVelocityContext(VelocityContext context, CodeConfig genTable)
    {
        CodeConfig subTable = genTable.getSubTable();
        String subTableName = genTable.getSubTableName();
        String subTableFkName = genTable.getSubTableFkName();
        String subClassName = genTable.getSubTable().getClassName();
        String subTableFkClassName = StringUtilsExternal.toCapitalizeCamelCase(subTableFkName);

        context.put("subTable", subTable);
        context.put("subTableName", subTableName);
        context.put("subTableFkName", subTableFkName);
        context.put("subTableFkClassName", subTableFkClassName);
        context.put("subTableFkclassName", StringUtils.uncapitalize(subTableFkClassName));
        context.put("subClassName", subClassName);
        context.put("subclassName", StringUtils.uncapitalize(subClassName));
        context.put("subImportList", getImportList(genTable.getSubTable()));
    }

    /**
     * 获取模板信息
     * @param tplCategory 生成的模板
     * @param tplWebType 前端类型
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory, String tplWebType)
    {
        String useWebType = "vm/vue";
        if ("element-plus".equals(tplWebType))
        {
            useWebType = "vm/vue/v3";
        }
        List<String> templates = new ArrayList<String>();
        templates.add("vm/java/domain.java.vm");
        templates.add("vm/java/dto.java.vm");
        templates.add("vm/java/repository.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/queryCriteria.java.vm");
//        templates.add("vm/sql/sql.vm");
        templates.add("vm/js/api.js.vm");
        if (GenConstants.TPL_CRUD.equals(tplCategory))
        {
            templates.add(useWebType + "/index.vue.vm");
        }
        else if (GenConstants.TPL_TREE.equals(tplCategory))
        {
            templates.add(useWebType + "/index-tree.vue.vm");
        }
        else if (GenConstants.TPL_SUB.equals(tplCategory))
        {
            templates.add(useWebType + "/index.vue.vm");
            templates.add("vm/java/sub-domain.java.vm");
        }
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, CodeConfig genTable)
    {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + StringUtils.replaceChars(packageName, ".", "/");
        String vuePath = "vue";

        if (template.contains("domain.java.vm"))
        {
            fileName = String.format("%s/domain/%s.java", javaPath, className);
        }
        if (template.contains("sub-domain.java.vm") && Objects.equals(GenConstants.TPL_SUB, genTable.getTplCategory()))
        {
            fileName = String.format("%s/domain/%s.java", javaPath, genTable.getSubTable().getClassName());
        }
        else if (template.contains("repository.java.vm"))
        {
            fileName = String.format("%s/repository/%sRepository.java", javaPath, className);
        }
        else if (template.contains("service.java.vm"))
        {
            fileName = String.format("%s/service/%sService.java", javaPath, className);
        }
        else if (template.contains("serviceImpl.java.vm"))
        {
            fileName = String.format("%s/service/impl/%sServiceImpl.java", javaPath, className);
        }
        else if (template.contains("controller.java.vm"))
        {
            fileName = String.format("%s/rest/%sController.java", javaPath, className);
        }
        else if (template.contains("mapper.java.vm"))
        {
            fileName = String.format("%s/service/mapstruct/%sMapper.java", javaPath, className);
        }
        else if (template.contains("dto.java.vm"))
        {
            fileName = String.format("%s/service/dto/%sDto.java", javaPath, className);
        }
        else if (template.contains("queryCriteria.java.vm"))
        {
            fileName = String.format("%s/service/dto/%sQueryCriteria.java", javaPath, className);
        }
        else if (template.contains("api.js.vm"))
        {
            fileName = String.format("%s/api/%s/%s.js", vuePath, moduleName, businessName);
        }
        else if (template.contains("index.vue.vm"))
        {
            fileName = String.format("%s/views/%s/%s/index.vue", vuePath, moduleName, businessName);
        }
        else if (template.contains("index-tree.vue.vm"))
        {
            fileName = String.format("%s/views/%s/%s/index.vue", vuePath, moduleName, businessName);
        }
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName)
    {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    /**
     * 根据列类型获取导入包
     * 
     * @param genTable 业务表对象
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(CodeConfig genTable) {
        List<ColumnInfo> columns = genTable.getColumns();
        CodeConfig subGenTable = genTable.getSubTable();
        HashSet<String> importList = new HashSet<String>();
        if (subGenTable!=null)   {
            importList.add("java.util.List");
        }
        for (ColumnInfo column : columns) {
            if (isNotSuperColumn(column.getSuperField()) && GenConstants.TYPE_DATE.equals(column.getJavaType()))  {
                importList.add("java.time.LocalDateTime");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            }
            else if (isNotSuperColumn(column.getSuperField()) && GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    /**
     * 根据列类型获取字典组
     * 
     * @param genTable 业务表对象
     * @return 返回字典组
     */
    public static String getDicts(CodeConfig genTable)
    {
        List<ColumnInfo> columns = genTable.getColumns();
        Set<String> dicts = new HashSet<String>();
        addDicts(dicts, columns);
        if ( genTable.getSubTable()!=null)
        {
            List<ColumnInfo> subColumns = genTable.getSubTable().getColumns();
            addDicts(dicts, subColumns);
        }
        return StringUtils.join(dicts, ", ");
    }

    /**
     * 添加字典列表
     * 
     * @param dicts 字典列表
     * @param columns 列集合
     */
    public static void addDicts(Set<String> dicts, List<ColumnInfo> columns)
    {
        for (ColumnInfo column : columns)
        {
            if (isNotSuperColumn(column.getSuperField()) && StringUtils.isNotEmpty(column.getDictType()) && StringUtilsExternal.equalsAnyIgnoreCase(
                    column.getHtmlType(),
                    GenConstants.HTML_SELECT, GenConstants.HTML_RADIO, GenConstants.HTML_CHECKBOX))
            {
                dicts.add("'" + column.getDictType() + "'");
            }
        }
    }

    /**
     * 获取权限前缀
     *
     * @param moduleName 模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String moduleName, String businessName)
    {
        return String.format("%s:%s", moduleName, businessName);
    }

    /**
     * 获取上级菜单ID字段
     *
     * @param paramsObj 生成其他选项
     * @return 上级菜单ID字段
     */
    public static String getParentMenuId(CodeConfigOptions paramsObj)  {
        if (paramsObj.parentMenuId()!=null){
            return paramsObj.parentMenuId().toString();
        }
        return DEFAULT_PARENT_MENU_ID;
    }

    /**
     * 获取树编码
     *
     * @param paramsObj 生成其他选项
     * @return 树编码
     */
    public static String getTreecode(CodeConfigOptions paramsObj)  {
        if (StringUtils.isNotBlank(paramsObj.treeCode()))  {
            return StringUtilsExternal.toCamelCase(paramsObj.treeCode());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取树父编码
     *
     * @param paramsObj 生成其他选项
     * @return 树父编码
     */
    public static String getTreeParentCode(CodeConfigOptions paramsObj)
    {
        if (StringUtils.isNotBlank(paramsObj.treeParentCode()))  {
            return StringUtilsExternal.toCamelCase(paramsObj.treeParentCode());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取树名称
     *
     * @param paramsObj 生成其他选项
     * @return 树名称
     */
    public static String getTreeName(CodeConfigOptions paramsObj)
    {
        if ( StringUtils.isNotBlank(paramsObj.treeName()) ) {
            return StringUtilsExternal.toCamelCase(paramsObj.treeName());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
     */
    public static int getExpandColumn(CodeConfig genTable)
    {
        String options = genTable.getOptions();
        CodeConfigOptions paramsObj = JsonUtils.parseObject(options, CodeConfigOptions.class);
        assert paramsObj != null;
        String treeName = paramsObj.treeName();
        int num = 0;
        for (ColumnInfo column : genTable.getColumns()) {
            if ("1".equals(column.getIsList())) {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(treeName))  {
                    break;
                }
            }
        }
        return num;
    }

    public static boolean isNotSuperColumn(String superField){
        return !"1".equals(superField);
    }
}
