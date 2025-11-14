package cc.efit.job.core;

import lombok.Data;

@Data
public class XxlJobInfo {
	/**
	 * 执行器主键ID
	 */
	private int jobGroup;
	/**
	 * 任务描述
	 */
	private String jobDesc;
	/**
	 * 负责人
	 */
	private String author = "SYSTEM";
	/**
     * 报警邮件
     */
	private String alarmEmail;
	/**
     * 调度类型
     */
	private String scheduleType = "CRON";
	/**
	 * 调度配置，值含义取决于调度类型
	 */
	private String scheduleConf;
	/**
     * 调度过期策略
     */
	private String misfireStrategy = "DO_NOTHING";
	/**
	 * 执行器路由策略
	 */
	private String executorRouteStrategy = "FIRST";
	/**
	 * 执行器，任务Handler名称
	 */
	private String executorHandler;
	/**
	 * 执行器，任务参数
	 */
	private String executorParam;
	/**
	 * 阻塞处理策略
	 */
	private String executorBlockStrategy = "SERIAL_EXECUTION";
	/**
	 * 任务执行超时时间，单位秒
	 */
	private int executorTimeout;
	/**
	 * 失败重试次数
	 */
	private int executorFailRetryCount;
	/**
	 * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
	 */
	private String glueType = "BEAN";
	/**
	 * GLUE源代码
	 */
	private String glueSource;
	/**
	 * GLUE备注
	 */
	private String glueRemark;
	/**
	 * 子任务ID，多个逗号分隔
	 */
	private String childJobId;
	/**
     * 调度状态：0-停止，1-运行
	 */
	private int triggerStatus;
}