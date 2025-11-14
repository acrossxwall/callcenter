package cc.efit.job;

import cc.efit.job.core.BasicXxlJob;
import cc.efit.job.core.XxlJobInfo;
import cc.efit.job.core.XxlJobResponse;
import cc.efit.json.utils.JsonUtils;
import cc.efit.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public interface IJobService {
    /**
     * 添加任务
     * @param jobInfo   任务信息
     * @return      纯数字代表任务id，其他错误原因
     */
    String addJobInfo(XxlJobInfo jobInfo) throws Exception;

    /**
     * 删除定时任务
     */
    String removeJobInfo(int id) throws Exception;

    /**
     * 更新任务状态
     * @param id            任务id
     * @param status 任务状态 1-启动 0-停止
     * @return               true-成功 false-失败
     */
    String updateJobStatus(int id, int status) throws Exception;

    /**
     * 更新任务信息
     * @param jobInfo      任务信息
     * @return             true-成功 false-失败
     */
    String updateJobInfo(XxlJobInfo jobInfo) throws Exception;

    /**
     * 触发任务
     * @param id     任务id
     * @return       true-成功 false-失败
     */
    String triggerJob(int id) throws Exception;

    default String  sendJson(String url , Map<String,Object> params) throws Exception {
        String json = HttpUtils.postForm(url,params);
        XxlJobResponse response = JsonUtils.parseObject(json, XxlJobResponse.class);
        return response.success()?response.content():response.msg();
    }

    default Map<String,Object> buildRequestParams(int id) {
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        return params;
    }

    default String addBasicJobHandler(BasicXxlJob job) throws Exception {
        XxlJobInfo jobInfo = new XxlJobInfo();
        jobInfo.setJobGroup(job.jobGroup());
        jobInfo.setExecutorHandler(job.jobHandlerName());
        jobInfo.setExecutorParam(job.executorParam());
        jobInfo.setScheduleConf(job.cron());
        jobInfo.setJobDesc(job.jobDesc());
        jobInfo.setAlarmEmail(job.alarmEmail());
        return addJobInfo(jobInfo);
    }
}
