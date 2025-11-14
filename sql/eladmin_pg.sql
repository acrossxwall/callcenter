/*
 Navicat Premium Data Transfer

 Source Server         : postgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 150001
 Source Host           : 192.168.247.129:5432
 Source Catalog        : callcenter
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 150001
 File Encoding         : 65001

 Date: 05/11/2025 20:43:49
*/

-- ----------------------------
-- Table structure for code_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."code_column";
CREATE TABLE "public"."code_column" (
  "column_id" serial4,
  "table_id" int4,
  "table_name" varchar(64) COLLATE "pg_catalog"."default",
  "column_name" varchar(200) COLLATE "pg_catalog"."default",
  "column_comment" varchar(500) COLLATE "pg_catalog"."default",
  "column_type" varchar(100) COLLATE "pg_catalog"."default",
  "java_type" varchar(500) COLLATE "pg_catalog"."default",
  "java_field" varchar(200) COLLATE "pg_catalog"."default",
  "is_pk" char(1) COLLATE "pg_catalog"."default",
  "is_increment" char(1) COLLATE "pg_catalog"."default",
  "is_required" char(1) COLLATE "pg_catalog"."default",
  "is_insert" char(1) COLLATE "pg_catalog"."default",
  "is_edit" char(1) COLLATE "pg_catalog"."default",
  "is_list" char(1) COLLATE "pg_catalog"."default",
  "is_query" char(1) COLLATE "pg_catalog"."default",
  "query_type" varchar(200) COLLATE "pg_catalog"."default" DEFAULT 'EQ'::character varying,
  "html_type" varchar(200) COLLATE "pg_catalog"."default",
  "dict_type" varchar(20) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sort" int4,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "super_field" char(1) COLLATE "pg_catalog"."default" DEFAULT 0
)
;
COMMENT ON COLUMN "public"."code_column"."column_id" IS '主键';
COMMENT ON COLUMN "public"."code_column"."table_id" IS '表id';
COMMENT ON COLUMN "public"."code_column"."table_name" IS '表名字';
COMMENT ON COLUMN "public"."code_column"."column_name" IS '列名称';
COMMENT ON COLUMN "public"."code_column"."column_comment" IS '列描述';
COMMENT ON COLUMN "public"."code_column"."column_type" IS '列类型';
COMMENT ON COLUMN "public"."code_column"."java_type" IS 'java类型';
COMMENT ON COLUMN "public"."code_column"."java_field" IS 'java字段名';
COMMENT ON COLUMN "public"."code_column"."is_pk" IS '是否主键 1-是';
COMMENT ON COLUMN "public"."code_column"."is_increment" IS '是否自增 1-是';
COMMENT ON COLUMN "public"."code_column"."is_required" IS '是否必填 1-是';
COMMENT ON COLUMN "public"."code_column"."is_insert" IS '是否为插入字段 1-是';
COMMENT ON COLUMN "public"."code_column"."is_edit" IS '是否为编辑字段1-是';
COMMENT ON COLUMN "public"."code_column"."is_list" IS '是否为列表字段 1-是';
COMMENT ON COLUMN "public"."code_column"."is_query" IS '是否为查询字段 1-是';
COMMENT ON COLUMN "public"."code_column"."query_type" IS '查询方式';
COMMENT ON COLUMN "public"."code_column"."html_type" IS '显示类型';
COMMENT ON COLUMN "public"."code_column"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."code_column"."sort" IS '排序';
COMMENT ON COLUMN "public"."code_column"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."code_column"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."code_column"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."code_column"."update_time" IS '更新时间';

-- ----------------------------
-- Table structure for code_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."code_config";
CREATE TABLE "public"."code_config" (
  "table_id" serial4,
  "table_name" varchar(200) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "table_comment" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sub_table_name" varchar(64) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "sub_table_fk_name" varchar(64) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "class_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "tpl_category" varchar(200) COLLATE "pg_catalog"."default" DEFAULT 'crud'::character varying,
  "tpl_web_type" varchar(30) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "package_name" varchar(100) COLLATE "pg_catalog"."default",
  "module_name" varchar(30) COLLATE "pg_catalog"."default",
  "business_name" varchar(30) COLLATE "pg_catalog"."default",
  "function_name" varchar(50) COLLATE "pg_catalog"."default",
  "function_author" varchar(50) COLLATE "pg_catalog"."default",
  "gen_type" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "gen_path" varchar(200) COLLATE "pg_catalog"."default" DEFAULT '/'::character varying,
  "options" varchar(1000) COLLATE "pg_catalog"."default",
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."code_config"."table_id" IS '主键';
COMMENT ON COLUMN "public"."code_config"."table_name" IS '表名';
COMMENT ON COLUMN "public"."code_config"."table_comment" IS '表描述';
COMMENT ON COLUMN "public"."code_config"."sub_table_name" IS '关联子表表名';
COMMENT ON COLUMN "public"."code_config"."sub_table_fk_name" IS '关联子表外键名';
COMMENT ON COLUMN "public"."code_config"."class_name" IS '实体类名';
COMMENT ON COLUMN "public"."code_config"."tpl_category" IS '使用模板 crud tree';
COMMENT ON COLUMN "public"."code_config"."tpl_web_type" IS '前端模板 element-ui element-plus';
COMMENT ON COLUMN "public"."code_config"."package_name" IS '包路径';
COMMENT ON COLUMN "public"."code_config"."module_name" IS '模块名';
COMMENT ON COLUMN "public"."code_config"."business_name" IS '业务名';
COMMENT ON COLUMN "public"."code_config"."function_name" IS '功能名';
COMMENT ON COLUMN "public"."code_config"."function_author" IS '功能作者';
COMMENT ON COLUMN "public"."code_config"."gen_type" IS '代码方式 1-自定义 0-zip压缩';
COMMENT ON COLUMN "public"."code_config"."gen_path" IS '生成路径';
COMMENT ON COLUMN "public"."code_config"."options" IS '其它选项';
COMMENT ON COLUMN "public"."code_config"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."code_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."code_config"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."code_config"."update_time" IS '更新时间';

-- ----------------------------
-- Table structure for efit_call_black_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_black_info";
CREATE TABLE "public"."efit_call_black_info" (
  "id" serial4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "source" int2,
  "expire_time" timestamp(6),
  "create_by" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "update_by" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) NOT NULL,
  "deleted" int2 NOT NULL DEFAULT 0,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_black_info"."id" IS '主键ID';
COMMENT ON COLUMN "public"."efit_call_black_info"."name" IS '用户姓名';
COMMENT ON COLUMN "public"."efit_call_black_info"."phone" IS '用户号码';
COMMENT ON COLUMN "public"."efit_call_black_info"."source" IS '来源，1:页面导入，2:api导入';
COMMENT ON COLUMN "public"."efit_call_black_info"."expire_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_black_info"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_black_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_black_info"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."efit_call_black_info"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_black_info"."deleted" IS '是否删除';
COMMENT ON COLUMN "public"."efit_call_black_info"."remark" IS '备注';
COMMENT ON COLUMN "public"."efit_call_black_info"."org_id" IS '机构id';
COMMENT ON TABLE "public"."efit_call_black_info" IS '黑名单库表';

-- ----------------------------
-- Table structure for efit_call_customer
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_customer";
CREATE TABLE "public"."efit_call_customer" (
  "id" serial4,
  "task_id" int4 NOT NULL,
  "phone" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "batch_id" int4,
  "batch_no" varchar(50) COLLATE "pg_catalog"."default",
  "case_id" varchar(32) COLLATE "pg_catalog"."default",
  "status" int2 DEFAULT 0,
  "called_status" int2 DEFAULT 0,
  "transfer_status" int2,
  "customer_info" json,
  "app_id" varchar(20) COLLATE "pg_catalog"."default",
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4,
  "call_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."efit_call_customer"."id" IS '客户ID';
COMMENT ON COLUMN "public"."efit_call_customer"."task_id" IS '关联任务ID';
COMMENT ON COLUMN "public"."efit_call_customer"."phone" IS '电话号码';
COMMENT ON COLUMN "public"."efit_call_customer"."batch_id" IS '批次id';
COMMENT ON COLUMN "public"."efit_call_customer"."batch_no" IS '批次号';
COMMENT ON COLUMN "public"."efit_call_customer"."case_id" IS '呼叫caseId';
COMMENT ON COLUMN "public"."efit_call_customer"."status" IS '呼叫状态：0-待呼叫 1-呼叫中 2-已呼叫';
COMMENT ON COLUMN "public"."efit_call_customer"."called_status" IS '0-未接听 1-已接听';
COMMENT ON COLUMN "public"."efit_call_customer"."transfer_status" IS '有值转接坐席 1-已接听 0-未接听';
COMMENT ON COLUMN "public"."efit_call_customer"."customer_info" IS '客户字典';
COMMENT ON COLUMN "public"."efit_call_customer"."app_id" IS '开放平台id';
COMMENT ON COLUMN "public"."efit_call_customer"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_customer"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_customer"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_customer"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_customer"."deleted" IS '删除标记';
COMMENT ON COLUMN "public"."efit_call_customer"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_customer"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_customer"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_call_customer"."call_id" IS '呼叫唯一标识';
COMMENT ON TABLE "public"."efit_call_customer" IS '客户名单表';

-- ----------------------------
-- Records of efit_call_customer
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_customer_batch
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_customer_batch";
CREATE TABLE "public"."efit_call_customer_batch" (
  "id" serial4,
  "task_id" int4 NOT NULL,
  "customer_count" int4,
  "success_count" int4,
  "fail_count" int4,
  "source" int2,
  "status" int2,
  "batch_no" varchar(50) COLLATE "pg_catalog"."default",
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_customer_batch"."id" IS '批次ID';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."task_id" IS '关联任务ID';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."customer_count" IS '客户数量';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."success_count" IS '成功数量';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."fail_count" IS '失败数量';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."source" IS '1-页面导入 2-API导入';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."status" IS '状态 0-未导入 1-导入成功 2-部分成功 3-失败';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."batch_no" IS '批次号';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."deleted" IS '删除标记';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_customer_batch"."user_id" IS '用户id';
COMMENT ON TABLE "public"."efit_call_customer_batch" IS '客户名单批次表';

-- ----------------------------
-- Records of efit_call_customer_batch
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_customer_import_detail
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_customer_import_detail";
CREATE TABLE "public"."efit_call_customer_import_detail" (
  "id" serial4,
  "task_id" int4 NOT NULL,
  "phone" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "batch_id" int4,
  "batch_no" varchar(50) COLLATE "pg_catalog"."default",
  "reason" varchar(50) COLLATE "pg_catalog"."default",
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."task_id" IS '关联任务ID';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."phone" IS '手机号';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."batch_id" IS '批次id';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."batch_no" IS '批次号';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."reason" IS '原因';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."deleted" IS '删除标记';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_customer_import_detail"."user_id" IS '用户id';
COMMENT ON TABLE "public"."efit_call_customer_import_detail" IS '客户名单批次详细表';

-- ----------------------------
-- Records of efit_call_customer_import_detail
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_line
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_line";
CREATE TABLE "public"."efit_call_line" (
  "id" serial4,
  "realm" varchar(32) COLLATE "pg_catalog"."default",
  "port" int4,
  "username" varchar(64) COLLATE "pg_catalog"."default",
  "password" varchar(64) COLLATE "pg_catalog"."default",
  "register" int2 DEFAULT 0,
  "create_by" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "deleted" int2 DEFAULT 0,
  "gateway_name" varchar(64) COLLATE "pg_catalog"."default",
  "reg_status" int2 DEFAULT 0,
  "line_name" varchar(255) COLLATE "pg_catalog"."default",
  "call_number" varchar(32) COLLATE "pg_catalog"."default",
  "concurrency" int4 NOT NULL,
  "unit_price" numeric(5,2),
  "org_id" int4 NOT NULL DEFAULT 0,
  "status" int2 DEFAULT 0,
  "call_prefix" varchar(32) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_line"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_call_line"."realm" IS '域名或ip';
COMMENT ON COLUMN "public"."efit_call_line"."port" IS '端口';
COMMENT ON COLUMN "public"."efit_call_line"."username" IS '认证的用户名';
COMMENT ON COLUMN "public"."efit_call_line"."password" IS '认证的密码';
COMMENT ON COLUMN "public"."efit_call_line"."register" IS '是否注册，默认0:非注册，1:注册';
COMMENT ON COLUMN "public"."efit_call_line"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_line"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_line"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_line"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_line"."deleted" IS '是否删除';
COMMENT ON COLUMN "public"."efit_call_line"."gateway_name" IS '网关名';
COMMENT ON COLUMN "public"."efit_call_line"."reg_status" IS '注册状态：0:初始，1:成功，2:失败';
COMMENT ON COLUMN "public"."efit_call_line"."line_name" IS '线路名称';
COMMENT ON COLUMN "public"."efit_call_line"."call_number" IS '主叫号码';
COMMENT ON COLUMN "public"."efit_call_line"."concurrency" IS '并发总数';
COMMENT ON COLUMN "public"."efit_call_line"."unit_price" IS '单价（元/分钟）';
COMMENT ON COLUMN "public"."efit_call_line"."org_id" IS '租户ID';
COMMENT ON COLUMN "public"."efit_call_line"."status" IS '状态：1:开，0:关';
COMMENT ON COLUMN "public"."efit_call_line"."call_prefix" IS '呼出前缀';
COMMENT ON COLUMN "public"."efit_call_line"."remark" IS '备注';
COMMENT ON TABLE "public"."efit_call_line" IS '中继线路网关表';

-- ----------------------------
-- Table structure for efit_call_line_assign
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_line_assign";
CREATE TABLE "public"."efit_call_line_assign" (
  "id" serial4,
  "line_id" int4,
  "assign_dept_id" int4,
  "concurrency" int4,
  "create_by" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "deleted" int2 DEFAULT 0,
  "org_id" int4 NOT NULL DEFAULT 0,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_line_assign"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_call_line_assign"."line_id" IS '线路id';
COMMENT ON COLUMN "public"."efit_call_line_assign"."assign_dept_id" IS '分配线路部门id';
COMMENT ON COLUMN "public"."efit_call_line_assign"."concurrency" IS '分配并发';
COMMENT ON COLUMN "public"."efit_call_line_assign"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_line_assign"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_line_assign"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_line_assign"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_line_assign"."deleted" IS '是否删除';
COMMENT ON COLUMN "public"."efit_call_line_assign"."org_id" IS '租户ID';
COMMENT ON TABLE "public"."efit_call_line_assign" IS '线路分配表';

-- ----------------------------
-- Table structure for efit_call_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_record";
CREATE TABLE "public"."efit_call_record" (
  "id" serial4,
  "task_id" int4 NOT NULL,
  "task_name" varchar(100) COLLATE "pg_catalog"."default",
  "call_template_id" int4 NOT NULL,
  "call_template_name" varchar(180) COLLATE "pg_catalog"."default",
  "customer_id" int4 NOT NULL,
  "call_id" varchar(32) COLLATE "pg_catalog"."default",
  "line_id" int4,
  "line_name" varchar(120) COLLATE "pg_catalog"."default",
  "call_number" varchar(20) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default",
  "customer_info" json,
  "call_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "answer_time" timestamp(6),
  "hangup_time" timestamp(6),
  "ring_seconds" int4,
  "hangup_reason" varchar(64) COLLATE "pg_catalog"."default",
  "intention_level" varchar(10) COLLATE "pg_catalog"."default",
  "intention_name" varchar(32) COLLATE "pg_catalog"."default",
  "duration" int4 DEFAULT 0,
  "status" int2,
  "record_path" varchar(120) COLLATE "pg_catalog"."default",
  "interactive_record" json,
  "interactive_count" int4,
  "tags" json,
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_record"."id" IS '记录ID';
COMMENT ON COLUMN "public"."efit_call_record"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."efit_call_record"."task_name" IS '任务名称';
COMMENT ON COLUMN "public"."efit_call_record"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_call_record"."call_template_name" IS '模板名称';
COMMENT ON COLUMN "public"."efit_call_record"."customer_id" IS '客户ID';
COMMENT ON COLUMN "public"."efit_call_record"."call_id" IS '呼叫id';
COMMENT ON COLUMN "public"."efit_call_record"."line_id" IS '线路id';
COMMENT ON COLUMN "public"."efit_call_record"."line_name" IS '线路名称';
COMMENT ON COLUMN "public"."efit_call_record"."call_number" IS '主叫';
COMMENT ON COLUMN "public"."efit_call_record"."phone" IS '外呼号码';
COMMENT ON COLUMN "public"."efit_call_record"."customer_info" IS '客户信息';
COMMENT ON COLUMN "public"."efit_call_record"."call_time" IS '呼叫时间';
COMMENT ON COLUMN "public"."efit_call_record"."answer_time" IS '接通时间';
COMMENT ON COLUMN "public"."efit_call_record"."hangup_time" IS '挂断时间';
COMMENT ON COLUMN "public"."efit_call_record"."ring_seconds" IS '振铃时长(秒)';
COMMENT ON COLUMN "public"."efit_call_record"."hangup_reason" IS '挂断原因';
COMMENT ON COLUMN "public"."efit_call_record"."intention_level" IS '意向等级';
COMMENT ON COLUMN "public"."efit_call_record"."intention_name" IS '意向描述';
COMMENT ON COLUMN "public"."efit_call_record"."duration" IS '通话时长(秒)';
COMMENT ON COLUMN "public"."efit_call_record"."status" IS '呼叫状态 0-未接听 1-已接听';
COMMENT ON COLUMN "public"."efit_call_record"."record_path" IS '录音地址';
COMMENT ON COLUMN "public"."efit_call_record"."interactive_record" IS '交互记录';
COMMENT ON COLUMN "public"."efit_call_record"."interactive_count" IS '交互轮次';
COMMENT ON COLUMN "public"."efit_call_record"."tags" IS '自定义标签';
COMMENT ON COLUMN "public"."efit_call_record"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_record"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_record"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_record"."deleted" IS '删除标记';
COMMENT ON COLUMN "public"."efit_call_record"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_record"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_record"."user_id" IS '用户id';
COMMENT ON TABLE "public"."efit_call_record" IS '呼叫记录表';

-- ----------------------------
-- Records of efit_call_record
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_system_statistics
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_system_statistics";
CREATE TABLE "public"."efit_call_system_statistics" (
  "id" serial4,
  "call_date" timestamp(6),
  "call_time" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "total_customers" int4 DEFAULT 0,
  "called_customers" int4 DEFAULT 0,
  "connect_count" int4 DEFAULT 0,
  "duration" int4 DEFAULT 0,
  "concurrent" int4 DEFAULT 0,
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_system_statistics"."id" IS 'ID';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."call_date" IS '统计日期';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."call_time" IS '统计时间';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."total_customers" IS '导入客户数';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."called_customers" IS '已呼叫客户数';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."connect_count" IS '接通用户数';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."duration" IS '通话时长';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."concurrent" IS '并发数';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."update_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."deleted" IS '是否删除 0-未删除 1-已删除';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_system_statistics"."user_id" IS '用户id';
COMMENT ON TABLE "public"."efit_call_system_statistics" IS '呼叫任务系统维度统计表';

-- ----------------------------
-- Records of efit_call_system_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_task";
CREATE TABLE "public"."efit_call_task" (
  "id" serial4,
  "task_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "call_template_id" int4,
  "template_name" varchar(100) COLLATE "pg_catalog"."default",
  "status" int2 DEFAULT 1,
  "call_status" int2 DEFAULT 0,
  "total_customers" int4 DEFAULT 0,
  "called_customers" int4 DEFAULT 0,
  "connect_count" int4 DEFAULT 0,
  "today_customers" int4 DEFAULT 0,
  "today_called" int4 DEFAULT 0,
  "today_connect" int4 DEFAULT 0,
  "allow_time" varchar(30) COLLATE "pg_catalog"."default",
  "deny_time" varchar(30) COLLATE "pg_catalog"."default",
  "allow_day" varchar(30) COLLATE "pg_catalog"."default",
  "enable_send_sms" int2,
  "sms_template_id" int4,
  "line_id" int4,
  "line_concurrent" int4 DEFAULT 5,
  "retry_open" int2 DEFAULT 0,
  "retry_condition" varchar(20) COLLATE "pg_catalog"."default",
  "retry_interval" int4,
  "retry_count" int4,
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "priority" int2
)
;
COMMENT ON COLUMN "public"."efit_call_task"."id" IS '任务ID';
COMMENT ON COLUMN "public"."efit_call_task"."task_name" IS '任务名称';
COMMENT ON COLUMN "public"."efit_call_task"."call_template_id" IS '关联话术模板ID';
COMMENT ON COLUMN "public"."efit_call_task"."template_name" IS '话术模板名称';
COMMENT ON COLUMN "public"."efit_call_task"."status" IS '任务显示状态：0-待启动 1-运行中';
COMMENT ON COLUMN "public"."efit_call_task"."call_status" IS '任务调度状态 0-未启动，1-运行中，2-暂停中 3-已暂停 4-已完成';
COMMENT ON COLUMN "public"."efit_call_task"."total_customers" IS '总导入客户数';
COMMENT ON COLUMN "public"."efit_call_task"."called_customers" IS '已呼叫客户数';
COMMENT ON COLUMN "public"."efit_call_task"."connect_count" IS '总接通用户数';
COMMENT ON COLUMN "public"."efit_call_task"."today_customers" IS '今天导入';
COMMENT ON COLUMN "public"."efit_call_task"."today_called" IS '今天已呼叫';
COMMENT ON COLUMN "public"."efit_call_task"."today_connect" IS '今天已接听';
COMMENT ON COLUMN "public"."efit_call_task"."allow_time" IS '允许外呼时间';
COMMENT ON COLUMN "public"."efit_call_task"."deny_time" IS '禁止外呼时间';
COMMENT ON COLUMN "public"."efit_call_task"."allow_day" IS '计划拨打日期';
COMMENT ON COLUMN "public"."efit_call_task"."enable_send_sms" IS '发送挂机短信 1-启用 0-禁用';
COMMENT ON COLUMN "public"."efit_call_task"."sms_template_id" IS '短信模板id';
COMMENT ON COLUMN "public"."efit_call_task"."line_id" IS '选择的线路id';
COMMENT ON COLUMN "public"."efit_call_task"."line_concurrent" IS '最大并发数';
COMMENT ON COLUMN "public"."efit_call_task"."retry_open" IS '1-重呼开启 0-关闭';
COMMENT ON COLUMN "public"."efit_call_task"."retry_condition" IS '重呼条件';
COMMENT ON COLUMN "public"."efit_call_task"."retry_interval" IS '重呼间隔';
COMMENT ON COLUMN "public"."efit_call_task"."retry_count" IS '重呼次数';
COMMENT ON COLUMN "public"."efit_call_task"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_task"."update_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_task"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_task"."deleted" IS '是否删除 0-未删除 1-已删除';
COMMENT ON COLUMN "public"."efit_call_task"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_task"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_task"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_call_task"."remark" IS '备注';
COMMENT ON COLUMN "public"."efit_call_task"."priority" IS '任务优先级 值1-10';
COMMENT ON TABLE "public"."efit_call_task" IS '呼叫任务表';

-- ----------------------------
-- Table structure for efit_call_task_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_task_job";
CREATE TABLE "public"."efit_call_task_job" (
  "id" serial4,
  "task_id" int4,
  "cron_expression" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "executor_handler" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_desc" varchar(255) COLLATE "pg_catalog"."default",
  "type" int2,
  "status" int2,
  "job_id" int4,
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4,
  "job_params" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."efit_call_task_job"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_call_task_job"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."efit_call_task_job"."cron_expression" IS 'cron表达式';
COMMENT ON COLUMN "public"."efit_call_task_job"."executor_handler" IS '执行器名称';
COMMENT ON COLUMN "public"."efit_call_task_job"."job_desc" IS '描述';
COMMENT ON COLUMN "public"."efit_call_task_job"."type" IS '任务类型：1:开启，2:暂停';
COMMENT ON COLUMN "public"."efit_call_task_job"."status" IS '状态：1:成功，2:失败';
COMMENT ON COLUMN "public"."efit_call_task_job"."job_id" IS 'xxlJob的ID';
COMMENT ON COLUMN "public"."efit_call_task_job"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_task_job"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."efit_call_task_job"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_task_job"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_task_job"."deleted" IS '是否删除';
COMMENT ON COLUMN "public"."efit_call_task_job"."org_id" IS '机构ID';
COMMENT ON COLUMN "public"."efit_call_task_job"."job_params" IS '定时任务参数';
COMMENT ON TABLE "public"."efit_call_task_job" IS '外呼任务job表';

-- ----------------------------
-- Records of efit_call_task_job
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_task_statistics
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_task_statistics";
CREATE TABLE "public"."efit_call_task_statistics" (
  "id" serial4,
  "task_id" int4 NOT NULL,
  "call_date" timestamp(6),
  "total_customers" int4 DEFAULT 0,
  "called_customers" int4 DEFAULT 0,
  "connect_count" int4 DEFAULT 0,
  "duration" int4 DEFAULT 0,
  "sms_count" int4 DEFAULT 0,
  "sms_success_count" int4 DEFAULT 0,
  "sms_bill_count" int4 DEFAULT 0,
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int2 NOT NULL DEFAULT 0,
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."efit_call_task_statistics"."id" IS 'ID';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."task_id" IS '任务id';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."call_date" IS '统计时间';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."total_customers" IS '导入客户数';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."called_customers" IS '已呼叫客户数';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."connect_count" IS '接通用户数';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."duration" IS '通话时长';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."sms_count" IS '短信发送量';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."sms_success_count" IS '短信成功量';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."sms_bill_count" IS '短信计费量';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."update_by" IS '创建人';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."deleted" IS '是否删除 0-未删除 1-已删除';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_task_statistics"."user_id" IS '用户id';
COMMENT ON TABLE "public"."efit_call_task_statistics" IS '呼叫任务统计表表';

-- ----------------------------
-- Records of efit_call_task_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for efit_call_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_template";
CREATE TABLE "public"."efit_call_template" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "name" varchar(180) COLLATE "pg_catalog"."default",
  "description" varchar(1000) COLLATE "pg_catalog"."default",
  "industry" varchar(20) COLLATE "pg_catalog"."default",
  "status" int2,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_call_template"."dept_id" IS '部门名称';
COMMENT ON COLUMN "public"."efit_call_template"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_call_template"."name" IS '话术名称';
COMMENT ON COLUMN "public"."efit_call_template"."description" IS '话术描述';
COMMENT ON COLUMN "public"."efit_call_template"."industry" IS '行业';
COMMENT ON COLUMN "public"."efit_call_template"."status" IS '状态';
COMMENT ON COLUMN "public"."efit_call_template"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_template"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_template"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_template"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."efit_call_template"."deleted" IS '是否删除 0-正常 1-删除';
COMMENT ON COLUMN "public"."efit_call_template"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."efit_call_template" IS 'ai拨打话术';

-- ----------------------------
-- Table structure for efit_call_template_operator
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_call_template_operator";
CREATE TABLE "public"."efit_call_template_operator" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "type" int2,
  "content" varchar(200) COLLATE "pg_catalog"."default",
  "ip" varchar(20) COLLATE "pg_catalog"."default",
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_call_template_operator"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_call_template_operator"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_call_template_operator"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_call_template_operator"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_call_template_operator"."type" IS '操作类型 1-增加 2-删除 3-修改';
COMMENT ON COLUMN "public"."efit_call_template_operator"."content" IS '操作内容';
COMMENT ON COLUMN "public"."efit_call_template_operator"."ip" IS '操作ip';
COMMENT ON COLUMN "public"."efit_call_template_operator"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_call_template_operator"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_call_template_operator"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_call_template_operator"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_call_template_operator"."deleted" IS '是否删除';
COMMENT ON TABLE "public"."efit_call_template_operator" IS '话术模板操作日志';

-- ----------------------------
-- Table structure for efit_sys_org
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_sys_org";
CREATE TABLE "public"."efit_sys_org" (
  "id" serial4,
  "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL DEFAULT 1,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "package_id" int4
)
;
COMMENT ON COLUMN "public"."efit_sys_org"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_sys_org"."name" IS '企业名称';
COMMENT ON COLUMN "public"."efit_sys_org"."status" IS '状态';
COMMENT ON COLUMN "public"."efit_sys_org"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_sys_org"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_sys_org"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_sys_org"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_sys_org"."package_id" IS '租户套餐id';

-- ----------------------------
-- Table structure for efit_sys_org_package
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_sys_org_package";
CREATE TABLE "public"."efit_sys_org_package" (
  "id" serial4,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "remark" varchar(256) COLLATE "pg_catalog"."default",
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int2 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."efit_sys_org_package"."id" IS '套餐编号';
COMMENT ON COLUMN "public"."efit_sys_org_package"."name" IS '套餐名';
COMMENT ON COLUMN "public"."efit_sys_org_package"."status" IS '租户状态（1正常 0停用）';
COMMENT ON COLUMN "public"."efit_sys_org_package"."remark" IS '备注';
COMMENT ON COLUMN "public"."efit_sys_org_package"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_sys_org_package"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_sys_org_package"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_sys_org_package"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_sys_org_package"."deleted" IS '是否删除';
COMMENT ON TABLE "public"."efit_sys_org_package" IS '租户套餐表';

-- ----------------------------
-- Records of efit_sys_org_package
-- ----------------------------

-- ----------------------------
-- Table structure for efit_sys_org_package_menus
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_sys_org_package_menus";
CREATE TABLE "public"."efit_sys_org_package_menus" (
  "id" serial4,
  "menu_id" int4 NOT NULL,
  "package_id" int4 NOT NULL,
  "create_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int2 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."menu_id" IS '菜单id';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."package_id" IS '租户套餐id';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_sys_org_package_menus"."deleted" IS '是否删除';
COMMENT ON TABLE "public"."efit_sys_org_package_menus" IS '租户套餐菜单关联表';

-- ----------------------------
-- Table structure for efit_template_flow
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_flow";
CREATE TABLE "public"."efit_template_flow" (
  "id" serial4,
  "node_name" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "verbal_ids" json,
  "call_template_id" int4,
  "enable_hangup" int2 DEFAULT 0,
  "hangup_mode" int2 DEFAULT 1,
  "enable_transfer" int2 DEFAULT 0,
  "transfer_number" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "transfer_type" int2 DEFAULT 1,
  "enable_interrupt" int2 DEFAULT 0,
  "enable_match_knowledge" int2 DEFAULT 1,
  "match_knowledge_ids" json,
  "ignore_reply_type" int2 DEFAULT 2,
  "sms_template_id" int4,
  "node_label" varchar(32) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "node_type" int2,
  "coordinate" varchar(256) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "org_id" int4,
  "user_id" int4,
  "transfer_agent_group_id" int4,
  "trigger_match" int2,
  "match_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "dept_id" int4,
  "deleted" int2 NOT NULL DEFAULT 0,
  "enable_send_sms" int2,
  "enable_ignore_reply" int2,
  "level_name" varchar(10) COLLATE "pg_catalog"."default",
  "verbal_type" int2,
  "target_flow_id" int4
)
;
COMMENT ON COLUMN "public"."efit_template_flow"."node_name" IS '节点名称';
COMMENT ON COLUMN "public"."efit_template_flow"."verbal_ids" IS '关联话术ID集合';
COMMENT ON COLUMN "public"."efit_template_flow"."call_template_id" IS '所属话术模板ID';
COMMENT ON COLUMN "public"."efit_template_flow"."enable_hangup" IS '是否挂断节点 0:否 1:是';
COMMENT ON COLUMN "public"."efit_template_flow"."hangup_mode" IS '挂机方式 0:回复话术后挂机 1:直接挂机';
COMMENT ON COLUMN "public"."efit_template_flow"."enable_transfer" IS '是否转人工节点 0:否 1:是';
COMMENT ON COLUMN "public"."efit_template_flow"."transfer_number" IS '转接坐席的手机号码';
COMMENT ON COLUMN "public"."efit_template_flow"."transfer_type" IS '转接方式 0:指定手机号码 1:指定坐席组';
COMMENT ON COLUMN "public"."efit_template_flow"."enable_interrupt" IS '允许被打断 0:否 1:是';
COMMENT ON COLUMN "public"."efit_template_flow"."enable_match_knowledge" IS '允许跳转知识库 0:否 1:是';
COMMENT ON COLUMN "public"."efit_template_flow"."match_knowledge_ids" IS '匹配知识库id列表';
COMMENT ON COLUMN "public"."efit_template_flow"."ignore_reply_type" IS '0:忽略用户回复 1:忽略用户除拒绝外的所有回复 2:都不忽略';
COMMENT ON COLUMN "public"."efit_template_flow"."sms_template_id" IS '短信模板ID';
COMMENT ON COLUMN "public"."efit_template_flow"."node_label" IS '自定义节点标签';
COMMENT ON COLUMN "public"."efit_template_flow"."node_type" IS '1:开始节点 2:过程节点 3:IVR节点 4:信息采集节点';
COMMENT ON COLUMN "public"."efit_template_flow"."coordinate" IS '节点坐标';
COMMENT ON COLUMN "public"."efit_template_flow"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_flow"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."efit_template_flow"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_flow"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_flow"."transfer_agent_group_id" IS '转接坐席组ID';
COMMENT ON COLUMN "public"."efit_template_flow"."trigger_match" IS '是否提取关键词预测 0-否 1-是';
COMMENT ON COLUMN "public"."efit_template_flow"."match_id" IS '关键词预测意图id';
COMMENT ON COLUMN "public"."efit_template_flow"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_flow"."update_by" IS '更新这';
COMMENT ON COLUMN "public"."efit_template_flow"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_flow"."deleted" IS '是否删除';
COMMENT ON COLUMN "public"."efit_template_flow"."enable_send_sms" IS '是否发送短信 0-否 1-是';
COMMENT ON COLUMN "public"."efit_template_flow"."enable_ignore_reply" IS '启用忽略用户回复';
COMMENT ON COLUMN "public"."efit_template_flow"."level_name" IS '设置意向等级';
COMMENT ON COLUMN "public"."efit_template_flow"."verbal_type" IS '0-顺序播报 1-随机播报';
COMMENT ON COLUMN "public"."efit_template_flow"."target_flow_id" IS '目标节点id';
COMMENT ON TABLE "public"."efit_template_flow" IS '流程节点';

-- ----------------------------
-- Table structure for efit_template_flow_branch
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_flow_branch";
CREATE TABLE "public"."efit_template_flow_branch" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "flow_id" int4,
  "intention_id" int4,
  "target_flow_id" int4,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "edge_info" json,
  "sort" int4
)
;
COMMENT ON COLUMN "public"."efit_template_flow_branch"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."call_template_id" IS '话术模板id';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."flow_id" IS '节点id';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."intention_id" IS '意图id';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."target_flow_id" IS '目标flowid';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."edge_info" IS '分支连线相关信息';
COMMENT ON COLUMN "public"."efit_template_flow_branch"."sort" IS '排序';
COMMENT ON TABLE "public"."efit_template_flow_branch" IS '节点意图分支';

-- ----------------------------
-- Table structure for efit_template_global_interaction
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_global_interaction";
CREATE TABLE "public"."efit_template_global_interaction" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "enable_interrupt" int2,
  "seconds" int4,
  "enable_interaction" int2,
  "max_interactive_count" int4,
  "max_duration" int4,
  "interaction_action" int2,
  "target_flow_id" int4,
  "verbal_id" int4,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_global_interaction"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."enable_interrupt" IS '启用打断设置';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."seconds" IS '多少秒不能打断';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."enable_interaction" IS '交互设置启用';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."max_interactive_count" IS '最大交互轮次';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."max_duration" IS '最大通话时长，单位：分钟';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."interaction_action" IS '触发动作 1-挂机 0-跳转节点';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."target_flow_id" IS '目标节点id';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."verbal_id" IS '话术id';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_global_interaction"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_global_interaction" IS '交互全局设置';

-- ----------------------------
-- Table structure for efit_template_global_noreply
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_global_noreply";
CREATE TABLE "public"."efit_template_global_noreply" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "enable_noreply" int2,
  "max_noreply_count" int4,
  "max_noreply_seconds" int4,
  "noreply_action" int2,
  "target_flow_id" int4,
  "verbal_id" int4,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_global_noreply"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."enable_noreply" IS '无应答设置启用';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."max_noreply_count" IS '最大无应答轮次';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."max_noreply_seconds" IS '最大无应答时长，单位：秒';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."noreply_action" IS '触发动作 1-挂机 0-跳转节点';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."target_flow_id" IS '目标节点id';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."verbal_id" IS '话术id';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_global_noreply"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_global_noreply" IS '无应答全局设置';

-- ----------------------------
-- Table structure for efit_template_global_tts
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_global_tts";
CREATE TABLE "public"."efit_template_global_tts" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "enable_tts" int2,
  "engine" varchar(20) COLLATE "pg_catalog"."default",
  "voice_type" varchar(20) COLLATE "pg_catalog"."default",
  "speed" varchar(20) COLLATE "pg_catalog"."default",
  "volume" varchar(20) COLLATE "pg_catalog"."default",
  "pitch" varchar(20) COLLATE "pg_catalog"."default",
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_global_tts"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_global_tts"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_global_tts"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_global_tts"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_template_global_tts"."enable_tts" IS 'tts设置';
COMMENT ON COLUMN "public"."efit_template_global_tts"."engine" IS '引擎';
COMMENT ON COLUMN "public"."efit_template_global_tts"."voice_type" IS '音色';
COMMENT ON COLUMN "public"."efit_template_global_tts"."speed" IS '语速';
COMMENT ON COLUMN "public"."efit_template_global_tts"."volume" IS '音量';
COMMENT ON COLUMN "public"."efit_template_global_tts"."pitch" IS '音调';
COMMENT ON COLUMN "public"."efit_template_global_tts"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_global_tts"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_global_tts"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_global_tts"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_global_tts"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_global_tts" IS 'tts全局设置';

-- ----------------------------
-- Table structure for efit_template_intention
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_intention";
CREATE TABLE "public"."efit_template_intention" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "keywords" text COLLATE "pg_catalog"."default",
  "sort" int4 NOT NULL DEFAULT 1,
  "type" int2,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "classify" int2
)
;
COMMENT ON COLUMN "public"."efit_template_intention"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_intention"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_intention"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_intention"."call_template_id" IS '话术模板id';
COMMENT ON COLUMN "public"."efit_template_intention"."name" IS '意图名称';
COMMENT ON COLUMN "public"."efit_template_intention"."keywords" IS '关键词';
COMMENT ON COLUMN "public"."efit_template_intention"."sort" IS '排序';
COMMENT ON COLUMN "public"."efit_template_intention"."type" IS '0-普通 1-默认不允许删除或修改';
COMMENT ON COLUMN "public"."efit_template_intention"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_intention"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_intention"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_intention"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_intention"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON COLUMN "public"."efit_template_intention"."classify" IS '意图分类属性 0 肯定  1 否定  2 拒绝  3 中性  4-其他';
COMMENT ON TABLE "public"."efit_template_intention" IS '话术意图';

-- ----------------------------
-- Table structure for efit_template_intention_level
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_intention_level";
CREATE TABLE "public"."efit_template_intention_level" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "description" varchar(20) COLLATE "pg_catalog"."default",
  "type" int2,
  "sort" int4 NOT NULL DEFAULT 1,
  "rule_content" varchar(1024) COLLATE "pg_catalog"."default",
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_intention_level"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_intention_level"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_intention_level"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_intention_level"."call_template_id" IS '话术模板id';
COMMENT ON COLUMN "public"."efit_template_intention_level"."name" IS '意向等级';
COMMENT ON COLUMN "public"."efit_template_intention_level"."description" IS '描述';
COMMENT ON COLUMN "public"."efit_template_intention_level"."type" IS '类型1-默认 0-普通';
COMMENT ON COLUMN "public"."efit_template_intention_level"."sort" IS '排序';
COMMENT ON COLUMN "public"."efit_template_intention_level"."rule_content" IS '规则内容';
COMMENT ON COLUMN "public"."efit_template_intention_level"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_intention_level"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_intention_level"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_intention_level"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_intention_level"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_intention_level" IS '话术意向等级';

-- ----------------------------
-- Table structure for efit_template_knowledge
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_knowledge";
CREATE TABLE "public"."efit_template_knowledge" (
  "id" serial4,
  "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "type" int2,
  "intention_id" int4,
  "verbal_ids" json NOT NULL,
  "action" int2 NOT NULL,
  "target_flow_id" int4,
  "enable_interrupt" int2,
  "sms_template_id" int4 DEFAULT 0,
  "agent_group_id" int4,
  "call_template_id" int4,
  "transfer_type" int2 DEFAULT 1,
  "transfer_number" varchar(20) COLLATE "pg_catalog"."default",
  "label" varchar(32) COLLATE "pg_catalog"."default",
  "org_id" int4,
  "user_id" int4,
  "dept_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_knowledge"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_knowledge"."name" IS '名称';
COMMENT ON COLUMN "public"."efit_template_knowledge"."type" IS '1:业务问题,2:一般问题';
COMMENT ON COLUMN "public"."efit_template_knowledge"."intention_id" IS '意图id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."verbal_ids" IS '话术 ids';
COMMENT ON COLUMN "public"."efit_template_knowledge"."action" IS '1表示回到主流程的下个节点2.回到跳出主流程节点3.回到跳出主流程的上个节点4.指定节点 param为指定节点的id5.挂断';
COMMENT ON COLUMN "public"."efit_template_knowledge"."target_flow_id" IS '多轮流程id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."enable_interrupt" IS '允许被打断 1:是 0:否';
COMMENT ON COLUMN "public"."efit_template_knowledge"."sms_template_id" IS '短信模板id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."agent_group_id" IS '坐席组id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."call_template_id" IS '话术模板id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."transfer_type" IS '电话号码 1 坐席组2';
COMMENT ON COLUMN "public"."efit_template_knowledge"."transfer_number" IS '转人工需要的手机号';
COMMENT ON COLUMN "public"."efit_template_knowledge"."label" IS '客户标签';
COMMENT ON COLUMN "public"."efit_template_knowledge"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."user_id" IS '创建人用户id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_knowledge"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_knowledge"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_knowledge"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_knowledge"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON COLUMN "public"."efit_template_knowledge"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."efit_template_knowledge" IS '知识库';

-- ----------------------------
-- Records of efit_template_knowledge
-- ----------------------------

-- ----------------------------
-- Table structure for efit_template_review
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_review";
CREATE TABLE "public"."efit_template_review" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "remark" varchar(240) COLLATE "pg_catalog"."default",
  "name" varchar(120) COLLATE "pg_catalog"."default",
  "status" int2,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "check_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_review"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_review"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_review"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_review"."call_template_id" IS '话术模板id';
COMMENT ON COLUMN "public"."efit_template_review"."remark" IS '审核备注';
COMMENT ON COLUMN "public"."efit_template_review"."name" IS '话术模板名称';
COMMENT ON COLUMN "public"."efit_template_review"."status" IS '2-审核中 3-审核通过 4-审核失败';
COMMENT ON COLUMN "public"."efit_template_review"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_review"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_review"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_review"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_review"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON COLUMN "public"."efit_template_review"."check_time" IS '审核时间';
COMMENT ON TABLE "public"."efit_template_review" IS '话术审核记录';

-- ----------------------------
-- Table structure for efit_template_verbal
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_verbal";
CREATE TABLE "public"."efit_template_verbal" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "content" varchar(1024) COLLATE "pg_catalog"."default",
  "type" int2,
  "source" int2,
  "status" int2,
  "recording" varchar(120) COLLATE "pg_catalog"."default",
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_verbal"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_verbal"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_verbal"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_verbal"."call_template_id" IS '话术模板id';
COMMENT ON COLUMN "public"."efit_template_verbal"."name" IS '来源名称知识库节点名字全局等';
COMMENT ON COLUMN "public"."efit_template_verbal"."content" IS '话术内容';
COMMENT ON COLUMN "public"."efit_template_verbal"."type" IS '类型1-tts 2-录音';
COMMENT ON COLUMN "public"."efit_template_verbal"."source" IS '1-节点 2-知识库 3-全局';
COMMENT ON COLUMN "public"."efit_template_verbal"."status" IS '1-已上传 0-未上传';
COMMENT ON COLUMN "public"."efit_template_verbal"."recording" IS '文件路径';
COMMENT ON COLUMN "public"."efit_template_verbal"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_verbal"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_verbal"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_verbal"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_verbal"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_verbal" IS '话术';

-- ----------------------------
-- Table structure for efit_template_words
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_words";
CREATE TABLE "public"."efit_template_words" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "category_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_words"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_words"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_words"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_words"."name" IS '词库';
COMMENT ON COLUMN "public"."efit_template_words"."category_id" IS '分类id';
COMMENT ON COLUMN "public"."efit_template_words"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_words"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_words"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_words"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_words" IS '关键词库';

-- ----------------------------
-- Table structure for efit_template_words_category
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_words_category";
CREATE TABLE "public"."efit_template_words_category" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "type" int2,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."efit_template_words_category"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_words_category"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_words_category"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_words_category"."name" IS '分类名称';
COMMENT ON COLUMN "public"."efit_template_words_category"."type" IS '1-通用词库 2-行业词库';
COMMENT ON COLUMN "public"."efit_template_words_category"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_words_category"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_words_category"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_words_category"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_words_category" IS '词库分类';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "id" serial4,
  "pid" int4,
  "sub_count" int4 DEFAULT 0,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dept_sort" int4 DEFAULT 999,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "org_id" int4,
  "status" int2,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."sys_dept"."id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dept"."pid" IS '上级部门';
COMMENT ON COLUMN "public"."sys_dept"."sub_count" IS '子部门数目';
COMMENT ON COLUMN "public"."sys_dept"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_dept"."dept_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dept"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."sys_dept"."status" IS '状态1-启用 0-禁用';
COMMENT ON TABLE "public"."sys_dept" IS '部门';

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict";
CREATE TABLE "public"."sys_dict" (
  "dict_id" serial4,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "dict_type" varchar(20) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_dict"."dict_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dict"."name" IS '字典名称';
COMMENT ON COLUMN "public"."sys_dict"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_dict"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dict"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict"."dict_type" IS '字典类型';
COMMENT ON TABLE "public"."sys_dict" IS '数据字典';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '部门状态', '部门状态', NULL, 'admin', '2019-10-27 20:31:36', 0, '2025-07-31 19:24:23.078373', 'dict_status');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '岗位状态', '岗位状态', NULL, 'admin', '2019-10-27 20:31:36', 0, '2025-07-31 19:24:41.149901', 'job_status');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '用户状态', '用户状态', NULL, 'admin', '2019-10-27 20:31:36', 0, '2025-07-31 19:24:57.203279', 'user_status');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '状态字典', NULL, 'admin', 'admin', '2025-08-01 10:14:06.839152', 0, '2025-08-01 10:14:06.839152', 'sys_normal_disable');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '显示隐藏', NULL, 'admin', 'admin', '2025-08-01 11:06:26.645552', 0, '2025-08-01 11:06:26.645552', 'sys_show_hide');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '用户性别', NULL, 'admin', 'admin', '2025-08-02 12:53:56.941038', 0, '2025-08-02 12:53:56.941118', 'sys_user_gender');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '话术模板状态', '话术模板状态', 'admin', 'admin', '2025-08-10 09:30:27.230263', 0, '2025-08-10 09:30:27.230263', 'template_status');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '操作类型', NULL, 'admin', 'admin', '2025-08-16 20:39:45.941046', 0, '2025-08-16 20:39:45.941046', 'operator_type');
INSERT INTO "public"."sys_dict" ("name","description","create_by","update_by","create_time","deleted","update_time","dict_type") VALUES( '意图分类属性', NULL, 'admin', 'admin', '2025-10-08 18:52:27.899796', 0, '2025-10-08 18:52:27.899796', 'intention_classify');

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_detail";
CREATE TABLE "public"."sys_dict_detail" (
  "detail_id" serial4,
  "label" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_sort" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "dict_type" varchar(20) COLLATE "pg_catalog"."default",
  "css_class" varchar(120) COLLATE "pg_catalog"."default",
  "list_class" varchar(120) COLLATE "pg_catalog"."default",
  "convert" int2 DEFAULT 1
)
;
COMMENT ON COLUMN "public"."sys_dict_detail"."detail_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dict_detail"."label" IS '字典标签';
COMMENT ON COLUMN "public"."sys_dict_detail"."value" IS '字典值';
COMMENT ON COLUMN "public"."sys_dict_detail"."dict_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dict_detail"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_detail"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_detail"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dict_detail"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict_detail"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."sys_dict_detail"."css_class" IS '字段样式';
COMMENT ON COLUMN "public"."sys_dict_detail"."list_class" IS '列表回显样式';
COMMENT ON COLUMN "public"."sys_dict_detail"."convert" IS '是否强制转换 1-是 0-否';
COMMENT ON TABLE "public"."sys_dict_detail" IS '数据字典详情';

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '激活', '1', 1, NULL, NULL, '2019-10-27 20:31:36', 0, NULL, 'user_status', NULL, NULL, 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '禁用', '0', 2, NULL, NULL, NULL, 0, NULL, 'user_status', NULL, NULL, 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '启用', '1', 1, NULL, NULL, NULL, 0, NULL, 'dict_status', NULL, NULL, 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '停用', '0', 2, NULL, NULL, '2019-10-27 20:31:36', 0, NULL, 'dict_status', NULL, NULL, 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '启用', '1', 1, NULL, NULL, NULL, 0, NULL, 'job_status', NULL, NULL, 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '停用', '0', 2, NULL, NULL, '2019-10-27 20:31:36', 0, NULL, 'job_status', NULL, NULL, 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '启用', '1', 0, 'admin', 'admin', '2025-08-01 10:14:26.599907', 0, '2025-08-01 21:23:08.965471', 'sys_normal_disable', NULL, 'primary', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '禁用', '0', 1, 'admin', 'admin', '2025-08-01 10:14:37.652781', 0, '2025-08-01 21:23:12.654738', 'sys_normal_disable', NULL, 'danger', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '显示', '1', 0, 'admin', 'admin', '2025-08-01 11:06:39.231852', 0, '2025-08-02 12:52:27.901373', 'sys_show_hide', NULL, 'primary', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '隐藏', '0', 0, 'admin', 'admin', '2025-08-01 11:06:47.16231', 0, '2025-08-02 12:52:35.025038', 'sys_show_hide', NULL, 'danger', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '帅哥', '男', 0, 'admin', 'admin', '2025-08-02 12:54:34.745801', 0, '2025-08-02 12:54:34.745801', 'sys_user_gender', NULL, 'default', 0);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '美女', '女', 0, 'admin', 'admin', '2025-08-02 12:54:47.512899', 0, '2025-08-02 12:54:47.512899', 'sys_user_gender', NULL, 'default', 0);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '新增', '1', 0, 'admin', 'admin', '2025-08-16 20:39:56.230624', 0, '2025-08-16 20:39:56.230624', 'operator_type', NULL, 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '删除', '2', 0, 'admin', 'admin', '2025-08-16 20:40:04.443386', 0, '2025-08-16 20:40:04.443386', 'operator_type', NULL, 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '修改', '3', 0, 'admin', 'admin', '2025-08-16 20:40:11.762575', 0, '2025-08-16 20:40:11.762575', 'operator_type', NULL, 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '审核驳回', '4', 4, 'admin', 'admin', '2025-08-10 09:31:42.052087', 0, '2025-08-30 16:01:06.725292', 'template_status', NULL, 'danger', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '审核通过', '3', 3, 'admin', 'admin', '2025-08-10 09:31:24.139394', 0, '2025-08-30 16:01:10.584341', 'template_status', NULL, 'success', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '审核中', '2', 2, 'admin', 'admin', '2025-08-10 09:31:07.554458', 0, '2025-08-30 16:01:13.3856', 'template_status', NULL, 'warning', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '编辑中', '1', 1, 'admin', 'admin', '2025-08-10 09:30:49.097999', 0, '2025-08-30 16:01:16.583579', 'template_status', NULL, 'primary', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( 'ddf', 'dddf', 1, 'admin', 'admin', '2025-07-31 20:09:52.570285', 1, '2025-07-31 20:15:37.124416', 'dict_status', NULL, 'danger', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '肯定', '0', 0, 'admin', 'admin', '2025-10-08 18:52:45.468635', 0, '2025-10-08 18:52:45.468635', 'intention_classify', NULL, 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '中性', '3', 3, 'admin', 'admin', '2025-10-08 18:53:28.031316', 0, '2025-10-08 18:53:28.031316', 'intention_classify', NULL, 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '其他', '4', 4, 'admin', 'admin', '2025-10-08 18:53:39.982233', 0, '2025-10-08 18:53:39.982233', 'intention_classify', NULL, 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '拒绝', '2', 2, 'admin', 'admin', '2025-10-08 18:53:07.775786', 0, '2025-10-08 18:53:48.141881', 'intention_classify', '', 'default', 1);
INSERT INTO "public"."sys_dict_detail" ("label","value","dict_sort","create_by","update_by","create_time","deleted","update_time","dict_type","css_class","list_class","convert")VALUES( '否定', '1', 1, 'admin', 'admin', '2025-10-08 18:52:56.573817', 0, '2025-10-08 18:53:51.802898', 'intention_classify', NULL, 'default', 1);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" serial4,
  "name" varchar(180) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL DEFAULT 1,
  "job_sort" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "org_id" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."sys_job"."job_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_job"."name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_job"."status" IS '状态';
COMMENT ON COLUMN "public"."sys_job"."job_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_job"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_job"."org_id" IS '机构id';
COMMENT ON TABLE "public"."sys_job" IS '岗位';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_log";
CREATE TABLE "public"."sys_log" (
  "log_id" serial4,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "log_type" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(255) COLLATE "pg_catalog"."default",
  "params" text COLLATE "pg_catalog"."default",
  "request_ip" varchar(255) COLLATE "pg_catalog"."default",
  "time" int8,
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "address" varchar(255) COLLATE "pg_catalog"."default",
  "browser" varchar(255) COLLATE "pg_catalog"."default",
  "exception_detail" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_log"."log_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_log"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_log"."log_type" IS '日志类型：INFI/ERROR';
COMMENT ON COLUMN "public"."sys_log"."method" IS '方法名';
COMMENT ON COLUMN "public"."sys_log"."params" IS '参数';
COMMENT ON COLUMN "public"."sys_log"."request_ip" IS '请求IP';
COMMENT ON COLUMN "public"."sys_log"."time" IS '执行时间';
COMMENT ON COLUMN "public"."sys_log"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_log"."address" IS '地址';
COMMENT ON COLUMN "public"."sys_log"."browser" IS '浏览器';
COMMENT ON COLUMN "public"."sys_log"."exception_detail" IS '异常';
COMMENT ON COLUMN "public"."sys_log"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_log" IS '系统日志';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
  "menu_id" serial4,
  "pid" int4,
  "sub_count" int4 DEFAULT 0,
  "type" int4,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "route_name" varchar(100) COLLATE "pg_catalog"."default",
  "component" varchar(255) COLLATE "pg_catalog"."default",
  "menu_sort" int4,
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "permission" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "frame" int2,
  "status" int2,
  "visible" int2,
  "query" varchar(120) COLLATE "pg_catalog"."default",
  "cache" int2 DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_menu"."menu_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_menu"."pid" IS '上级菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."sub_count" IS '子菜单数目';
COMMENT ON COLUMN "public"."sys_menu"."type" IS '菜单类型';
COMMENT ON COLUMN "public"."sys_menu"."title" IS '菜单标题';
COMMENT ON COLUMN "public"."sys_menu"."route_name" IS '组件名称';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."sys_menu"."menu_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '图标';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '链接地址';
COMMENT ON COLUMN "public"."sys_menu"."permission" IS '权限';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_menu"."frame" IS '是否iframe 1-是 0-否';
COMMENT ON COLUMN "public"."sys_menu"."status" IS '状态1-启用 0-禁用';
COMMENT ON COLUMN "public"."sys_menu"."visible" IS '是否显示 1-是 0-否';
COMMENT ON COLUMN "public"."sys_menu"."query" IS '路由参数';
COMMENT ON COLUMN "public"."sys_menu"."cache" IS '是否缓存 1-是 0-否';
COMMENT ON TABLE "public"."sys_menu" IS '系统菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 53, 'markdown', 'markdown', NULL, NULL, NULL, '2019-03-08 13:46:44', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 54, 'dev', 'yaml', NULL, NULL, NULL, '2019-03-08 15:49:40', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', NULL, NULL, NULL, '2019-01-04 16:23:57', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (21, NULL, 2, 0, '多级菜单', NULL, '', 900, 'menu', 'nested', NULL, NULL, 'admin', '2019-01-04 16:22:03', 1, '2020-06-21 17:27:35', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', NULL, NULL, NULL, '2018-12-27 10:13:09', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', NULL, NULL, NULL, '2019-01-04 16:24:48', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', NULL, NULL, NULL, '2019-01-07 17:27:32', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', NULL, NULL, 'admin', '2019-01-04 16:23:29', 1, '2020-06-21 17:27:20', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', NULL, NULL, NULL, '2018-12-19 13:38:49', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 52, 'fwb', 'tinymce', NULL, NULL, NULL, '2018-12-27 11:58:25', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (10, NULL, 5, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', NULL, NULL, NULL, '2018-12-19 13:38:16', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'people', 'online', NULL, NULL, 'admin', '2019-10-26 22:08:43', 0, '2025-08-03 21:34:21.90665', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 18, 'dashboard', 'druid', NULL, NULL, 'admin', '2018-12-18 15:19:34', 0, '2025-08-03 21:34:59.415902', 0, 1, 0, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (30, 36, 0, 1, '代码生成', 'GeneratorIndex', 'tool/gen/index', 32, 'build', 'generator', NULL, NULL, 'admin', '2019-01-11 15:45:55', 0, '2025-08-09 20:32:01.714457', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (116, 36, 0, 1, '生成预览', 'Preview', 'generator/preview', 999, 'swagger', 'generator/preview/:tableName', NULL, NULL, 'admin', '2019-11-26 14:54:36', 0, '2025-08-03 21:36:03.835276', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (32, 6, 0, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 12, 'bug', 'errorLog', NULL, NULL, 'admin', '2019-01-13 13:49:03', 1, '2025-08-03 21:34:39.757498', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', 'system:user:list', NULL, NULL, '2018-12-18 15:14:44', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (1, NULL, 6, 0, '系统管理', NULL, NULL, 1, 'system', 'system', NULL, NULL, 'admin', '2018-12-18 15:11:29', 0, '2025-01-14 15:48:18', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (90, NULL, 5, 1, '运维管理', 'Mnt', '', 20, 'mnt', 'mnt', NULL, NULL, NULL, '2019-11-09 10:31:08', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (117, 4, 0, 1, '租户管理员', 'OrgUsers', 'system/org/user', 2, 'user', 'users', 'system:org:list', 'admin', 'admin', '2025-08-07 10:55:21.418', 0, '2025-08-07 10:55:21.418', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (36, NULL, 5, 0, '系统工具', NULL, '', 30, 'tool', 'sys-tools', NULL, NULL, 'admin', '2019-03-29 10:57:35', 0, '2025-08-01 12:00:51.201832', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (110, 94, 0, 2, '部署编辑', NULL, '', 999, '', '', 'system:deploy:edit', NULL, NULL, '2019-11-17 11:11:41', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (92, 90, 3, 1, '服务器', 'ServerDeploy', 'maint/server/index', 22, 'server', 'maint/serverDeploy', 'system:serverDeploy:list', NULL, NULL, '2019-11-10 10:29:25', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (93, 90, 3, 1, '应用管理', 'App', 'maint/app/index', 23, 'app', 'maint/app', 'system:app:list', NULL, NULL, '2019-11-10 11:05:16', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (94, 90, 3, 1, '部署管理', 'Deploy', 'maint/deploy/index', 24, 'deploy', 'maint/deploy', 'system:deploy:list', NULL, NULL, '2019-11-10 15:56:55', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (125, 122, 5, 1, '黑名单管理', 'Black', 'call/black/index', 3, 'bug', 'black', 'call:black:list', 'admin', 'admin', '2025-08-27 20:14:19.263077', 0, '2025-08-28 20:39:32.840701', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (127, 122, 0, 1, '任务监控', 'TaskMonitor', 'call/task/components/TaskMonitor', 4, 'monitor', 'monitor', 'call:task:list', 'admin', 'admin', '2025-08-31 15:40:31.956864', 0, '2025-08-31 15:43:17.585853', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (111, 94, 0, 2, '部署删除', NULL, '', 999, '', '', 'system:deploy:remove', NULL, NULL, '2019-11-17 11:12:01', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (102, 97, 0, 2, '删除', NULL, '', 999, '', '', 'system:deployHistory:remove', NULL, NULL, '2019-11-17 09:32:48', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (120, 119, 5, 1, '话术模板', 'TemplateList', 'dialogue/template/index', 1, 'cascader', 'template', 'dialogue:template:list', 'admin', 'admin', '2025-08-07 13:36:17.791', 0, '2025-08-07 13:36:17.791', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (28, 1, 0, 1, '任务调度', 'Timing', 'system/timing/index', 999, 'time', 'timing', 'system:timing:list', NULL, 'admin', '2019-01-07 20:34:40', 1, '2025-08-01 12:00:00.611184', 0, 0, 0, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (4, NULL, 3, 0, '租户管理', NULL, NULL, 4, 'server', 'tenant', NULL, 'admin', 'admin', '2025-08-05 09:32:58.624', 0, '2025-08-07 10:00:50.418', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (126, 119, 0, 1, '审核管理', 'TemplateReview', 'dialogue/review/review', 3, 'validCode', 'review', 'dialogue:review:list', 'admin', 'admin', '2025-08-30 15:58:30.510625', 0, '2025-08-30 15:58:30.510625', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (119, NULL, 3, 0, '话术管理', NULL, NULL, 5, 'international', 'dialogue', NULL, 'admin', 'admin', '2025-08-07 12:46:08.036', 0, '2025-08-07 12:46:14.966', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/operlog/index', 11, 'log', 'logs', NULL, NULL, 'admin', '2018-12-18 15:18:26', 0, '2025-08-30 16:22:23.418946', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (118, 4, 4, 1, '系统租户', 'SysOrg', 'system/org/index', 1, 'logininfor', 'org', 'system:org:list', 'admin', 'admin', '2025-08-07 09:53:38.035', 0, '2025-08-07 09:53:38.035', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (6, NULL, 4, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', NULL, NULL, NULL, '2018-12-18 15:17:48', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (123, 122, 5, 1, '呼叫任务', 'CallTask', 'call/task/index', 1, 'monitor', 'task', 'call:task:list', 'admin', 'admin', '2025-08-24 19:42:18.867723', 0, '2025-08-27 20:12:22.67125', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (122, NULL, 4, 0, '呼叫中心', NULL, NULL, 6, 'job', 'call', NULL, 'admin', 'admin', '2025-08-24 19:40:51.902997', 0, '2025-08-27 20:11:38.82336', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'tree-table', 'menu', 'system:menu:list', NULL, 'admin', '2018-12-18 15:17:28', 0, '2025-08-01 11:57:08.717906', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'theme', 'role', 'system:roles:list', NULL, 'admin', '2018-12-18 15:16:07', 0, '2025-08-01 11:57:42.296747', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'tree', 'dept', 'system:dept:list', NULL, 'admin', '2019-03-25 09:46:00', 0, '2025-08-01 11:58:21.837531', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'job', 'job', 'system:job:list', NULL, 'admin', '2019-03-29 13:51:18', 0, '2025-08-01 11:58:46.773286', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'select', 'dict', 'system:dict:list', NULL, 'admin', '2019-04-10 11:49:04', 0, '2025-08-01 11:59:18.982028', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', 'system:user:add', NULL, NULL, '2019-10-29 10:59:46', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (124, 122, 5, 1, '线路管理', 'Line', 'call/line/index', 2, 'phone', 'line', 'call:line:list', 'admin', 'admin', '2025-08-27 20:13:22.413825', 0, '2025-08-27 20:13:22.413825', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (121, 119, 9, 1, '关键词库', 'Words', 'dialogue/words/index', 2, 'education', 'words', 'dialogue:words:list', 'admin', 'admin', '2025-08-19 20:18:58.733', 0, '2025-08-19 20:18:58.733', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', 'system:user:edit', NULL, NULL, '2019-10-29 11:00:08', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', 'system:roles:add', NULL, NULL, '2019-10-29 12:45:34', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', 'system:roles:edit', NULL, NULL, '2019-10-29 12:46:16', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', 'system:menu:add', NULL, NULL, '2019-10-29 12:55:07', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', 'system:menu:edit', NULL, NULL, '2019-10-29 12:55:40', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', 'system:dept:add', NULL, NULL, '2019-10-29 12:57:09', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', 'system:dept:edit', NULL, NULL, '2019-10-29 12:57:27', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', 'system:job:add', NULL, NULL, '2019-10-29 12:58:27', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', 'system:job:edit', NULL, NULL, '2019-10-29 12:58:45', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', 'system:dict:add', NULL, NULL, '2019-10-29 13:00:17', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', 'system:dict:edit', NULL, NULL, '2019-10-29 13:00:42', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', 'system:storage:add', NULL, NULL, '2019-10-29 13:09:09', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', 'system:storage:edit', NULL, NULL, '2019-10-29 13:09:22', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 50, 'chart', 'echarts', 'system:', NULL, NULL, '2019-11-21 09:04:32', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (98, 90, 3, 1, '数据库管理', 'Database', 'maint/database/index', 26, 'database', 'maint/database', 'system:database:list', NULL, NULL, '2019-11-10 20:40:04', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (97, 90, 1, 1, '部署备份', 'DeployHistory', 'maint/deployHistory/index', 25, 'backup', 'maint/deployHistory', 'system:deployHistory:list', NULL, NULL, '2019-11-10 16:49:44', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 14, 'code', 'server', 'system:monitor:list', NULL, 'admin', '2019-11-07 13:06:39', 0, '2025-08-03 21:35:12.180569', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (82, 36, 0, 1, '表单生成', '', 'tool/build/index', 33, 'form', 'build', 'system:', NULL, 'admin', '2019-11-17 20:08:56', 0, '2025-08-09 20:33:33.615829', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'upload', 'storage', 'system:storage:list', NULL, 'admin', '2018-12-31 11:12:15', 0, '2025-08-03 21:35:54.608031', 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (103, 92, 0, 2, '服务器新增', NULL, '', 999, '', '', 'system:serverDeploy:add', NULL, NULL, '2019-11-17 11:08:33', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (104, 92, 0, 2, '服务器编辑', NULL, '', 999, '', '', 'system:serverDeploy:edit', NULL, NULL, '2019-11-17 11:08:57', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (106, 93, 0, 2, '应用新增', NULL, '', 999, '', '', 'system:app:add', NULL, NULL, '2019-11-17 11:10:03', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (107, 93, 0, 2, '应用编辑', NULL, '', 999, '', '', 'system:app:edit', NULL, NULL, '2019-11-17 11:10:28', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (109, 94, 0, 2, '部署新增', NULL, '', 999, '', '', 'system:deploy:add', NULL, NULL, '2019-11-17 11:11:22', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', 'system:user:remove', NULL, NULL, '2019-10-29 11:00:23', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', 'system:roles:remove', NULL, NULL, '2019-10-29 12:46:51', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', 'system:menu:remove', NULL, NULL, '2019-10-29 12:56:00', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', 'system:dept:remove', NULL, NULL, '2019-10-29 12:57:41', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', 'system:job:remove', NULL, NULL, '2019-10-29 12:59:04', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', 'system:dict:remove', NULL, NULL, '2019-10-29 13:00:59', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', 'system:storage:remove', NULL, NULL, '2019-10-29 13:09:34', 0, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (105, 92, 0, 2, '服务器删除', NULL, '', 999, '', '', 'system:serverDeploy:remove', NULL, NULL, '2019-11-17 11:09:15', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (108, 93, 0, 2, '应用删除', NULL, '', 999, '', '', 'system:app:remove', NULL, NULL, '2019-11-17 11:10:55', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (129, 118, 0, 2, '租户新增', NULL, NULL, 1, NULL, NULL, 'system:org:add', 'admin', 'admin', '2025-11-05 20:21:17.370197', 0, '2025-11-05 20:21:17.370197', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (130, 118, 0, 2, '租户编辑', NULL, NULL, 2, NULL, NULL, 'system:org:edit', 'admin', 'admin', '2025-11-05 20:21:47.136272', 0, '2025-11-05 20:21:47.136272', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (131, 118, 0, 2, '租户删除', NULL, NULL, 3, NULL, NULL, 'system:org:remove', 'admin', 'admin', '2025-11-05 20:22:06.459517', 0, '2025-11-05 20:22:06.459517', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (132, 118, 0, 2, '租户导出', NULL, NULL, 4, NULL, NULL, 'system:org:export', 'admin', 'admin', '2025-11-05 20:23:26.816762', 0, '2025-11-05 20:23:41.33936', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (133, 128, 0, 2, '套餐新增', NULL, NULL, 1, NULL, NULL, 'org:package:add', 'admin', 'admin', '2025-11-05 20:24:07.443868', 0, '2025-11-05 20:24:07.443868', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (128, 4, 3, 1, '租户套餐', 'Package', 'system/org/package', 3, 'component', 'org/package', 'org:package:list', 'admin', 'admin', '2025-10-28 11:58:48.679', 0, '2025-10-28 11:58:48.679', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (134, 128, 0, 2, '套餐编辑', NULL, NULL, 2, NULL, NULL, 'org:package:edit', 'admin', 'admin', '2025-11-05 20:24:33.349608', 0, '2025-11-05 20:24:33.349608', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (145, 121, 0, 2, '词库分类列表', NULL, NULL, 5, NULL, NULL, 'dialogue:category:list', 'admin', 'admin', '2025-11-05 20:32:49.780672', 0, '2025-11-05 20:32:49.780672', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (135, 128, 0, 2, '套餐删除', NULL, NULL, 3, NULL, NULL, 'org:package:remove', 'admin', 'admin', '2025-11-05 20:24:54.786278', 0, '2025-11-05 20:24:54.786278', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (136, 120, 0, 2, '话术新增', NULL, NULL, 1, NULL, NULL, 'dialogue:template:add', 'admin', 'admin', '2025-11-05 20:26:45.227526', 0, '2025-11-05 20:26:45.227526', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (137, 120, 0, 2, '话术编辑', NULL, NULL, 2, NULL, NULL, 'dialogue:template:edit', 'admin', 'admin', '2025-11-05 20:27:11.733641', 0, '2025-11-05 20:27:11.733641', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (138, 120, 0, 2, '话术删除', NULL, NULL, 3, NULL, NULL, 'dialogue:template:remove', 'admin', 'admin', '2025-11-05 20:27:30.737472', 0, '2025-11-05 20:27:30.737472', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (139, 120, 0, 2, '话术详情', NULL, NULL, 4, NULL, NULL, 'dialogue:template:query', 'admin', 'admin', '2025-11-05 20:27:51.095215', 0, '2025-11-05 20:27:51.095215', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (140, 120, 0, 2, '话术导出', NULL, NULL, 5, NULL, NULL, 'dialogue:template:export', 'admin', 'admin', '2025-11-05 20:28:53.012823', 0, '2025-11-05 20:28:53.012823', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (142, 121, 0, 2, '词库分类编辑', NULL, NULL, 2, NULL, NULL, 'dialogue:category:edit', 'admin', 'admin', '2025-11-05 20:31:23.569586', 0, '2025-11-05 20:31:23.569586', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (141, 121, 0, 2, '词库类查询', NULL, NULL, 1, NULL, NULL, 'dialogue:category:query', 'admin', 'admin', '2025-11-05 20:30:47.722846', 0, '2025-11-05 20:31:32.232054', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (143, 121, 0, 2, '词库分类删除', NULL, NULL, 3, NULL, NULL, 'dialogue:category:remove', 'admin', 'admin', '2025-11-05 20:31:56.156806', 0, '2025-11-05 20:31:56.156806', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (144, 121, 0, 2, '词库分类新增', NULL, NULL, 4, NULL, NULL, 'dialogue:category:add', 'admin', 'admin', '2025-11-05 20:32:25.821636', 0, '2025-11-05 20:32:25.821636', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (146, 121, 0, 2, '词库添加', NULL, NULL, 6, NULL, NULL, 'dialogue:words:add', 'admin', 'admin', '2025-11-05 20:33:23.119481', 0, '2025-11-05 20:33:23.119481', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (148, 121, 0, 2, '词库删除', NULL, NULL, 8, NULL, NULL, 'dialogue:words:remove', 'admin', 'admin', '2025-11-05 20:34:03.356397', 0, '2025-11-05 20:34:03.356397', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (147, 121, 0, 2, '词库编辑', NULL, NULL, 7, NULL, NULL, 'dialogue:words:edit', 'admin', 'admin', '2025-11-05 20:33:41.795037', 0, '2025-11-05 20:34:10.279125', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (149, 121, 0, 2, '词库导出', NULL, NULL, 9, NULL, NULL, 'dialogue:words:export', 'admin', 'admin', '2025-11-05 20:34:28.81318', 0, '2025-11-05 20:34:28.81318', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (150, 123, 0, 2, '任务添加', NULL, NULL, 1, NULL, NULL, 'call:task:add', 'admin', 'admin', '2025-11-05 20:35:29.760978', 0, '2025-11-05 20:35:29.760978', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', 'system:timing:add', NULL, NULL, '2019-10-29 13:07:28', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', 'system:timing:edit', NULL, NULL, '2019-10-29 13:07:41', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', 'system:timing:remove', NULL, NULL, '2019-10-29 13:07:54', 1, NULL, 0, 1, 1, NULL, 0);
INSERT INTO "public"."sys_menu" VALUES (152, 123, 0, 2, '任务编辑', NULL, NULL, 2, NULL, NULL, 'call:task:edit', 'admin', 'admin', '2025-11-05 20:37:27.276194', 0, '2025-11-05 20:37:27.276194', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (153, 123, 0, 2, '任务删除', NULL, NULL, 3, NULL, NULL, 'call:task:remove', 'admin', 'admin', '2025-11-05 20:37:42.549203', 0, '2025-11-05 20:37:42.549203', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (154, 123, 0, 2, '任务查看', NULL, NULL, 4, NULL, NULL, 'call:task:query', 'admin', 'admin', '2025-11-05 20:38:04.727881', 0, '2025-11-05 20:38:04.727881', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (155, 123, 0, 2, '任务导出', NULL, NULL, 5, NULL, NULL, 'call:task:export', 'admin', 'admin', '2025-11-05 20:38:28.694248', 0, '2025-11-05 20:38:28.694248', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (156, 124, 0, 2, '线路添加', NULL, NULL, 1, NULL, NULL, 'call:line:add', 'admin', 'admin', '2025-11-05 20:39:39.996202', 0, '2025-11-05 20:39:39.996202', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (157, 124, 0, 2, '线路编辑', NULL, NULL, 2, NULL, NULL, 'call:line:edit', 'admin', 'admin', '2025-11-05 20:40:04.060143', 0, '2025-11-05 20:40:04.060143', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (158, 124, 0, 2, '线路删除', NULL, NULL, 3, NULL, NULL, 'call:line:remove', 'admin', 'admin', '2025-11-05 20:40:24.325628', 0, '2025-11-05 20:40:24.325628', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (159, 124, 0, 2, '线路导出', NULL, NULL, 4, NULL, NULL, 'call:line:export', 'admin', 'admin', '2025-11-05 20:40:38.114999', 0, '2025-11-05 20:40:38.114999', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (160, 124, 0, 2, '线路查询', NULL, NULL, 5, NULL, NULL, 'call:line:query', 'admin', 'admin', '2025-11-05 20:40:54.691332', 0, '2025-11-05 20:40:54.691332', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (161, 125, 0, 2, '黑名单添加', NULL, NULL, 1, NULL, NULL, 'call:black:add', 'admin', 'admin', '2025-11-05 20:41:20.320863', 0, '2025-11-05 20:41:20.320863', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (162, 125, 0, 2, '黑名单编辑', NULL, NULL, 2, NULL, NULL, 'call:black:edit', 'admin', 'admin', '2025-11-05 20:41:33.868371', 0, '2025-11-05 20:41:33.868371', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (163, 125, 0, 2, '黑名单删除', NULL, NULL, 3, NULL, NULL, 'call:black:remove', 'admin', 'admin', '2025-11-05 20:41:50.557105', 0, '2025-11-05 20:41:50.557105', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (164, 125, 0, 2, '黑名单查询', NULL, NULL, 4, NULL, NULL, 'call:black:query', 'admin', 'admin', '2025-11-05 20:42:08.77316', 0, '2025-11-05 20:42:08.77316', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (165, 125, 0, 2, '黑名单导出', NULL, NULL, 5, NULL, NULL, 'call:black:export', 'admin', 'admin', '2025-11-05 20:42:30.82898', 0, '2025-11-05 20:42:30.82898', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (167, 166, 0, 2, '通话记录查看', NULL, NULL, 1, NULL, NULL, 'call:record:query', 'admin', 'admin', '2025-11-08 19:15:30.373771', 0, '2025-11-08 19:15:30.373771', 0, 1, 1, NULL, 1);
INSERT INTO "public"."sys_menu" VALUES (166, 122, 1, 1, '通话记录', 'Record', 'call/record/index', 5, 'list', 'record', 'call:record:list', 'admin', 'admin', '2025-11-08 19:14:52.730662', 0, '2025-11-08 19:14:52.730662', 0, 1, 1, NULL, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "role_id" serial4,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "data_scope" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "org_id" int4,
  "role_key" varchar COLLATE "pg_catalog"."default",
  "role_sort" int4,
  "dept_id" int4,
  "user_id" int4
)
;
COMMENT ON COLUMN "public"."sys_role"."role_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_role"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_role"."status" IS '状态1-启用 0-禁用';
COMMENT ON COLUMN "public"."sys_role"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据权限';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "public"."sys_role"."role_sort" IS '角色排序';
COMMENT ON COLUMN "public"."sys_role"."dept_id" IS '创建人部门id';
COMMENT ON COLUMN "public"."sys_role"."user_id" IS '创建人userid';
COMMENT ON TABLE "public"."sys_role" IS '角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" ("name","status","description","data_scope","create_by","update_by","create_time","deleted","update_time","org_id","role_key","role_sort","dept_id","user_id") VALUES (  '管理员', 1, '-', '1', NULL, 'admin', '2018-11-23 11:04:37', 0, '2025-11-01 17:26:10.646615', NULL, 'admin', 1, NULL, NULL);

-- ----------------------------
-- Table structure for sys_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_roles_depts";
CREATE TABLE "public"."sys_roles_depts" (
  "role_id" int4 NOT NULL,
  "dept_id" int4 NOT NULL,
  "org_id" int4
)
;
COMMENT ON COLUMN "public"."sys_roles_depts"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_roles_depts"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_roles_depts"."org_id" IS '机构id';
COMMENT ON TABLE "public"."sys_roles_depts" IS '角色部门关联';

-- ----------------------------
-- Records of sys_roles_depts
-- ----------------------------

-- ----------------------------
-- Table structure for sys_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_roles_menus";
CREATE TABLE "public"."sys_roles_menus" (
  "menu_id" int4 NOT NULL,
  "role_id" int4 NOT NULL,
  "org_id" int4
)
;
COMMENT ON COLUMN "public"."sys_roles_menus"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_roles_menus"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_roles_menus"."org_id" IS '机构id';
COMMENT ON TABLE "public"."sys_roles_menus" IS '角色菜单关联';

-- ----------------------------
-- Records of sys_roles_menus
-- ----------------------------
INSERT INTO "public"."sys_roles_menus" VALUES (116, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (1, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (117, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (118, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (119, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (120, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (6, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (121, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (7, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (122, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (9, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (123, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (14, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (124, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (125, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (19, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (126, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (127, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (30, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (128, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (1, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (3, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (36, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (4, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (5, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (6, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (7, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (9, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (14, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (18, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (30, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (35, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (36, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (37, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (39, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (41, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (44, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (45, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (46, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (48, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (49, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (50, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (52, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (53, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (54, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (56, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (57, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (58, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (60, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (61, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (62, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (80, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (64, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (65, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (66, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (116, 2, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (77, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (78, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (79, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (80, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (82, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (119, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (120, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (121, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (126, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (122, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (123, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (124, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (125, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (127, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (1, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (3, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (35, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (37, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (44, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (45, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (46, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (48, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (49, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (50, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (56, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (57, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (58, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (60, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (61, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (62, 3, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (167, 1, NULL);
INSERT INTO "public"."sys_roles_menus" VALUES (166, 1, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "id" serial4,
  "user_dept_id" int4,
  "username" varchar(180) COLLATE "pg_catalog"."default",
  "nick_name" varchar(255) COLLATE "pg_catalog"."default",
  "gender" varchar(2) COLLATE "pg_catalog"."default",
  "phone" varchar(255) COLLATE "pg_catalog"."default",
  "email" varchar(180) COLLATE "pg_catalog"."default",
  "avatar_name" varchar(255) COLLATE "pg_catalog"."default",
  "avatar_path" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "pwd_update_time" timestamp(6),
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "org_id" int4,
  "login_ip" varchar(120) COLLATE "pg_catalog"."default",
  "login_date" timestamp(6),
  "status" int2,
  "user_type" int2 DEFAULT 0,
  "user_id" int4,
  "dept_id" int4
)
;
COMMENT ON COLUMN "public"."sys_user"."id" IS 'ID';
COMMENT ON COLUMN "public"."sys_user"."user_dept_id" IS '部门名称';
COMMENT ON COLUMN "public"."sys_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '昵称';
COMMENT ON COLUMN "public"."sys_user"."gender" IS '性别';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_user"."avatar_name" IS '头像地址';
COMMENT ON COLUMN "public"."sys_user"."avatar_path" IS '头像真实路径';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user"."pwd_update_time" IS '修改密码的时间';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."sys_user"."login_ip" IS '最后登录id';
COMMENT ON COLUMN "public"."sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user"."status" IS '状态1-启用 0-禁用';
COMMENT ON COLUMN "public"."sys_user"."user_type" IS '1-管理员 0-普通';
COMMENT ON TABLE "public"."sys_user" IS '系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" ("user_dept_id",  "username",  "nick_name",  "gender",  "phone",  "email",  "avatar_name",  "avatar_path",  "password",  "create_by",  "update_by",  "pwd_update_time",  "create_time",  "deleted",  "update_time",  "org_id",  "login_ip",  "login_date",  "status",  "user_type",  "user_id",  "dept_id") VALUES ( NULL, 'admin', '管理员', '男', '18888888888', '201507802@qq.com', 'avatar-20250122102642222.png', '/Users/jie/Documents/work/private/eladmin/~/avatar/avatar-20250122102642222.png', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', NULL, 'admin', '2020-05-03 16:38:31', '2018-08-23 09:11:56', 0, '2025-01-22 10:26:42', 1, '192.168.10.6', '2025-11-05 20:17:43.357', 1, 1, NULL, NULL);

-- ----------------------------
-- Table structure for sys_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_users_jobs";
CREATE TABLE "public"."sys_users_jobs" (
  "user_id" int4 NOT NULL,
  "job_id" int4 NOT NULL,
  "org_id" int4
)
;
COMMENT ON COLUMN "public"."sys_users_jobs"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_users_jobs"."job_id" IS '岗位ID';
COMMENT ON COLUMN "public"."sys_users_jobs"."org_id" IS '机构id';
COMMENT ON TABLE "public"."sys_users_jobs" IS '用户与岗位关联表';

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_users_roles";
CREATE TABLE "public"."sys_users_roles" (
  "user_id" int4 NOT NULL,
  "role_id" int4 NOT NULL,
  "org_id" int4
)
;
COMMENT ON COLUMN "public"."sys_users_roles"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_users_roles"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_users_roles"."org_id" IS '机构id';
COMMENT ON TABLE "public"."sys_users_roles" IS '用户角色关联';

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
INSERT INTO "public"."sys_users_roles" VALUES (1, 1, NULL);
INSERT INTO "public"."sys_users_roles" VALUES (1, 2, NULL);
-- ----------------------------
-- Table structure for tool_email_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_email_config";
CREATE TABLE "public"."tool_email_config" (
  "config_id" serial4,
  "from_user" varchar(255) COLLATE "pg_catalog"."default",
  "host" varchar(255) COLLATE "pg_catalog"."default",
  "pass" varchar(255) COLLATE "pg_catalog"."default",
  "port" varchar(255) COLLATE "pg_catalog"."default",
  "user" varchar(255) COLLATE "pg_catalog"."default",
  "org_id" int4
)
;
COMMENT ON COLUMN "public"."tool_email_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_email_config"."from_user" IS '收件人';
COMMENT ON COLUMN "public"."tool_email_config"."host" IS '邮件服务器SMTP地址';
COMMENT ON COLUMN "public"."tool_email_config"."pass" IS '密码';
COMMENT ON COLUMN "public"."tool_email_config"."port" IS '端口';
COMMENT ON COLUMN "public"."tool_email_config"."user" IS '发件者用户名';
COMMENT ON COLUMN "public"."tool_email_config"."org_id" IS '机构id';
COMMENT ON TABLE "public"."tool_email_config" IS '邮箱配置';

-- ----------------------------
-- Records of tool_email_config
-- ----------------------------

-- ----------------------------
-- Table structure for tool_local_storage
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_local_storage";
CREATE TABLE "public"."tool_local_storage" (
  "storage_id" serial4,
  "real_name" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(100) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  "org_id" int4,
  "used" int2 NOT NULL DEFAULT 0,
  "user_id" int4,
  "dept_id" int4
)
;
COMMENT ON COLUMN "public"."tool_local_storage"."storage_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_local_storage"."real_name" IS '文件真实的名称';
COMMENT ON COLUMN "public"."tool_local_storage"."name" IS '文件名';
COMMENT ON COLUMN "public"."tool_local_storage"."suffix" IS '后缀';
COMMENT ON COLUMN "public"."tool_local_storage"."path" IS '路径';
COMMENT ON COLUMN "public"."tool_local_storage"."type" IS '类型';
COMMENT ON COLUMN "public"."tool_local_storage"."size" IS '大小';
COMMENT ON COLUMN "public"."tool_local_storage"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."tool_local_storage"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."tool_local_storage"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."tool_local_storage"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."tool_local_storage"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."tool_local_storage"."used" IS '文件是否使用 1-是 0-否';
COMMENT ON COLUMN "public"."tool_local_storage"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."tool_local_storage"."dept_id" IS '部门id';
COMMENT ON TABLE "public"."tool_local_storage" IS '本地存储';

-- ----------------------------
-- Records of tool_local_storage
-- ----------------------------
DROP TABLE IF EXISTS "public"."efit_template_global_nlu";
CREATE TABLE "public"."efit_template_global_nlu" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "enable_nlu" int2,
  "mode_id" varchar(64),
  "threshold" numeric(3, 2),
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  primary key(id)
)
;
COMMENT ON COLUMN "public"."efit_template_global_nlu"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."enable_nlu" IS 'nlu设置启用';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."mode_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."threshold" IS '匹配阈值';

COMMENT ON COLUMN "public"."efit_template_global_nlu"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_global_nlu"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_global_nlu" IS 'nlu全局设置';


 DROP TABLE IF EXISTS "public"."efit_template_global_default_verbal";
CREATE TABLE "public"."efit_template_global_default_verbal" (
  "id" serial4,
  "dept_id" int4,
  "user_id" int4,
  "call_template_id" int4,
  "enable_default" int2,
  "default_action" int2,
  "target_flow_id" int4,
  "verbal_id" int4,
  "org_id" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "deleted" int2 NOT NULL DEFAULT 0,
  "update_time" timestamp(6),
  primary key(id)
)
;
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."id" IS '主键';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."call_template_id" IS '模板id';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."enable_default" IS '兜底话术设置启用';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."default_action" IS '触发动作 1-挂机 0-跳转节点';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."target_flow_id" IS '目标节点id';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."verbal_id" IS '话术id';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."org_id" IS '机构id';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."efit_template_global_default_verbal"."deleted" IS '是否删除 0-否 1-是';
COMMENT ON TABLE "public"."efit_template_global_default_verbal" IS '兜底话术全局设置';
-- ----------------------------
-- Primary Key structure for table code_column
-- ----------------------------
ALTER TABLE "public"."code_column" ADD CONSTRAINT "code_column_pkey" PRIMARY KEY ("column_id");

-- ----------------------------
-- Primary Key structure for table code_config
-- ----------------------------
ALTER TABLE "public"."code_config" ADD CONSTRAINT "code_config_pkey" PRIMARY KEY ("table_id");

-- ----------------------------
-- Primary Key structure for table efit_call_black_info
-- ----------------------------
ALTER TABLE "public"."efit_call_black_info" ADD CONSTRAINT "efit_dispatch_black_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table efit_call_customer
-- ----------------------------
CREATE INDEX "idx_phone" ON "public"."efit_call_customer" USING btree (
  "phone" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_status" ON "public"."efit_call_customer" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_task_id_status" ON "public"."efit_call_customer" USING btree (
  "task_id" "pg_catalog"."int4_ops" ASC NULLS LAST,
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table efit_call_customer
-- ----------------------------
ALTER TABLE "public"."efit_call_customer" ADD CONSTRAINT "efit_dispatch_call_customer_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_customer_batch
-- ----------------------------
ALTER TABLE "public"."efit_call_customer_batch" ADD CONSTRAINT "efit_dispatch_call_customer_batch_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_customer_import_detail
-- ----------------------------
ALTER TABLE "public"."efit_call_customer_import_detail" ADD CONSTRAINT "efit_dispatch_call_customer_import_detail_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_line
-- ----------------------------
ALTER TABLE "public"."efit_call_line" ADD CONSTRAINT "efit_dispatch_line_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_line_assign
-- ----------------------------
ALTER TABLE "public"."efit_call_line_assign" ADD CONSTRAINT "efit_dispatch_line_assign_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table efit_call_record
-- ----------------------------
CREATE INDEX "idx_task_id" ON "public"."efit_call_record" USING btree (
  "task_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table efit_call_record
-- ----------------------------
ALTER TABLE "public"."efit_call_record" ADD CONSTRAINT "efit_dispatch_call_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_system_statistics
-- ----------------------------
ALTER TABLE "public"."efit_call_system_statistics" ADD CONSTRAINT "efit_call_system_statistics_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_task
-- ----------------------------
ALTER TABLE "public"."efit_call_task" ADD CONSTRAINT "efit_dispatch_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_task_job
-- ----------------------------
ALTER TABLE "public"."efit_call_task_job" ADD CONSTRAINT "efit_call_task_job_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_task_statistics
-- ----------------------------
ALTER TABLE "public"."efit_call_task_statistics" ADD CONSTRAINT "efit_call_task_statistics_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_template
-- ----------------------------
ALTER TABLE "public"."efit_call_template" ADD CONSTRAINT "efit_call_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_call_template_operator
-- ----------------------------
ALTER TABLE "public"."efit_call_template_operator" ADD CONSTRAINT "efit_call_template_operator_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_sys_org
-- ----------------------------
ALTER TABLE "public"."efit_sys_org" ADD CONSTRAINT "sys_org_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_sys_org_package
-- ----------------------------
ALTER TABLE "public"."efit_sys_org_package" ADD CONSTRAINT "efit_sys_org_package_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_sys_org_package_menus
-- ----------------------------
ALTER TABLE "public"."efit_sys_org_package_menus" ADD CONSTRAINT "efit_sys_org_package_menus_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_flow
-- ----------------------------
ALTER TABLE "public"."efit_template_flow" ADD CONSTRAINT "efit_template_flow_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_flow_branch
-- ----------------------------
ALTER TABLE "public"."efit_template_flow_branch" ADD CONSTRAINT "efit_template_flow_branch_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_global_interaction
-- ----------------------------
ALTER TABLE "public"."efit_template_global_interaction" ADD CONSTRAINT "efit_template_global_interaction_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_global_noreply
-- ----------------------------
ALTER TABLE "public"."efit_template_global_noreply" ADD CONSTRAINT "efit_template_global_noreply_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_global_tts
-- ----------------------------
ALTER TABLE "public"."efit_template_global_tts" ADD CONSTRAINT "efit_template_global_tts_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_intention
-- ----------------------------
ALTER TABLE "public"."efit_template_intention" ADD CONSTRAINT "efit_template_intention_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_intention_level
-- ----------------------------
ALTER TABLE "public"."efit_template_intention_level" ADD CONSTRAINT "efit_template_intention_level_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_knowledge
-- ----------------------------
ALTER TABLE "public"."efit_template_knowledge" ADD CONSTRAINT "efit_template_knowledge_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_review
-- ----------------------------
ALTER TABLE "public"."efit_template_review" ADD CONSTRAINT "efit_template_review_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_verbal
-- ----------------------------
ALTER TABLE "public"."efit_template_verbal" ADD CONSTRAINT "efit_template_verbal_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_words
-- ----------------------------
ALTER TABLE "public"."efit_template_words" ADD CONSTRAINT "efit_template_words_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table efit_template_words_category
-- ----------------------------
ALTER TABLE "public"."efit_template_words_category" ADD CONSTRAINT "efit_template_words_category_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_dept
-- ----------------------------
CREATE INDEX "sys_dept_idx_pid" ON "public"."sys_dept" USING btree (
  "pid" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_dict
-- ----------------------------
ALTER TABLE "public"."sys_dict" ADD CONSTRAINT "sys_dict_pkey" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Primary Key structure for table sys_dict_detail
-- ----------------------------
ALTER TABLE "public"."sys_dict_detail" ADD CONSTRAINT "sys_dict_detail_pkey" PRIMARY KEY ("detail_id");

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job" ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id");

-- ----------------------------
-- Indexes structure for table sys_log
-- ----------------------------
CREATE INDEX "sys_log_idx_create_time_index" ON "public"."sys_log" USING btree (
  "create_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "sys_log_idx_log_type" ON "public"."sys_log" USING btree (
  "log_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_log
-- ----------------------------
ALTER TABLE "public"."sys_log" ADD CONSTRAINT "sys_log_pkey" PRIMARY KEY ("log_id");

-- ----------------------------
-- Indexes structure for table sys_menu
-- ----------------------------
CREATE INDEX "sys_menu_idx_pid" ON "public"."sys_menu" USING btree (
  "pid" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "sys_menu_uniq_name" ON "public"."sys_menu" USING btree (
  "route_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "sys_menu_uniq_title" ON "public"."sys_menu" USING btree (
  "title" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("menu_id");
SELECT setval('"public"."sys_menu_menu_id_seq"', 168, true);
-- ----------------------------
-- Indexes structure for table sys_role
-- ----------------------------
CREATE INDEX "sys_role_idx_level" ON "public"."sys_role" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "sys_role_uniq_name" ON "public"."sys_role" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Indexes structure for table sys_roles_depts
-- ----------------------------
CREATE INDEX "sys_roles_depts_idx_dept_id" ON "public"."sys_roles_depts" USING btree (
  "dept_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "sys_roles_depts_idx_role_id" ON "public"."sys_roles_depts" USING btree (
  "role_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_roles_depts
-- ----------------------------
ALTER TABLE "public"."sys_roles_depts" ADD CONSTRAINT "sys_roles_depts_pkey" PRIMARY KEY ("role_id", "dept_id");

-- ----------------------------
-- Indexes structure for table sys_roles_menus
-- ----------------------------
CREATE INDEX "sys_roles_menus_idx_menu_id" ON "public"."sys_roles_menus" USING btree (
  "menu_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "sys_roles_menus_idx_role_id" ON "public"."sys_roles_menus" USING btree (
  "role_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_roles_menus
-- ----------------------------
ALTER TABLE "public"."sys_roles_menus" ADD CONSTRAINT "sys_roles_menus_pkey" PRIMARY KEY ("menu_id", "role_id");

-- ----------------------------
-- Indexes structure for table sys_user
-- ----------------------------
CREATE INDEX "sys_user_idx_dept_id" ON "public"."sys_user" USING btree (
  "user_dept_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "sys_user_uniq_email" ON "public"."sys_user" USING btree (
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "sys_user_uniq_username" ON "public"."sys_user" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_users_jobs
-- ----------------------------
CREATE INDEX "sys_users_jobs_idx_job_id" ON "public"."sys_users_jobs" USING btree (
  "job_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "sys_users_jobs_idx_user_id" ON "public"."sys_users_jobs" USING btree (
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_users_jobs
-- ----------------------------
ALTER TABLE "public"."sys_users_jobs" ADD CONSTRAINT "sys_users_jobs_pkey" PRIMARY KEY ("user_id", "job_id");

-- ----------------------------
-- Indexes structure for table sys_users_roles
-- ----------------------------
CREATE INDEX "sys_users_roles_idx_role_id" ON "public"."sys_users_roles" USING btree (
  "role_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "sys_users_roles_idx_user_id" ON "public"."sys_users_roles" USING btree (
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_users_roles
-- ----------------------------
ALTER TABLE "public"."sys_users_roles" ADD CONSTRAINT "sys_users_roles_pkey" PRIMARY KEY ("user_id", "role_id");

-- ----------------------------
-- Primary Key structure for table tool_email_config
-- ----------------------------
ALTER TABLE "public"."tool_email_config" ADD CONSTRAINT "tool_email_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table tool_local_storage
-- ----------------------------
ALTER TABLE "public"."tool_local_storage" ADD CONSTRAINT "tool_local_storage_pkey" PRIMARY KEY ("storage_id");
