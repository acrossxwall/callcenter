package cc.efit.job.core;

import cc.efit.job.IJobService;
import cc.efit.job.constants.XxlJobConstants;
import cc.efit.utils.ObjectToMapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {
    private final AdminProperties jobProperties;
    @Override
    public String addJobInfo(XxlJobInfo jobInfo) throws Exception {
        log.info("addJobInfo:{}", jobInfo);
        Map<String, Object> params = ObjectToMapUtils.convertToMap(jobInfo);
        String result = sendJson(jobProperties.getAddresses() + XxlJobConstants.JOB_ADD_URL, params);
        log.info("addJobInfo result:{}", result);
        return result;
    }

    @Override
    public String removeJobInfo(int id)  throws Exception{
        Map<String,Object> params = buildRequestParams(id);
        String result = sendJson(jobProperties.getAddresses() + XxlJobConstants.JOB_REMOVE_URL, params);
        log.info("remove job result:{}", result);
        return result;
    }

    @Override
    public String updateJobStatus(int id, int status)  throws Exception {
        Map<String,Object> params = buildRequestParams(id);
        String url ;
        if (status == 1) {
            url =jobProperties.getAddresses() + XxlJobConstants.JOB_START_URL;
        }else{
            url =jobProperties.getAddresses() + XxlJobConstants.JOB_STOP_URL;
        }
        String result = sendJson(url, params);
        log.info("update job status:{}", result);
        return result;
    }

    @Override
    public String updateJobInfo(XxlJobInfo jobInfo)  throws Exception {
        log.info("updateJobInfo:{}",jobInfo);
        Map<String, Object> params = ObjectToMapUtils.convertToMap(jobInfo);
        String result = sendJson(jobProperties.getAddresses() + XxlJobConstants.JOB_UPDATE_URL, params);
        log.info("updateJobInfo result:{}", result);
        return result;
    }

    @Override
    public String triggerJob(int id)  throws Exception {
        Map<String,Object> params = buildRequestParams(id);
        String result = sendJson(jobProperties.getAddresses() + XxlJobConstants.JOB_TRIGGER_URL, params);
        log.info("trigger job  result:{}", result);
        return result;
    }
}
