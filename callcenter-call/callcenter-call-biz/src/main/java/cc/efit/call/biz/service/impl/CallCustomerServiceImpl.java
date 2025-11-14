package cc.efit.call.biz.service.impl;

import cc.efit.dialogue.api.constants.TemplateKeyConstants;
import cc.efit.dialogue.api.vo.template.TemplateInitInfo;
import cc.efit.json.utils.JsonUtils;
import cc.efit.utils.RegexUtils;
import cc.efit.db.utils.QueryHelp;
import cc.efit.dialogue.api.constants.TemplateConstants;
import cc.efit.dialogue.api.service.DialogueTemplateApi;
import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.enums.CustomerBatchEnum;
import cc.efit.call.api.enums.CustomerSourceEnum;
import cc.efit.call.api.enums.ImportCustomerTypeEnum;
import cc.efit.call.biz.config.ImportDataExecutor;
import cc.efit.call.biz.domain.CallCustomerBatch;
import cc.efit.call.biz.domain.CallCustomerImportDetail;
import cc.efit.call.api.domain.CallTask;
import cc.efit.call.biz.repository.CallCustomerBatchRepository;
import cc.efit.call.biz.repository.CallCustomerImportDetailRepository;
import cc.efit.call.api.repository.CallTaskRepository;
import cc.efit.call.biz.vo.customer.CustomerItemInfo;
import cc.efit.call.biz.vo.customer.ExcelCustomerRowHandler;
import cc.efit.call.biz.vo.customer.ImportCustomerInfo;
import cc.efit.domain.LocalStorage;
import cc.efit.exception.BadRequestException;
import cc.efit.org.GlobalPermissionHolder;
import cc.efit.redis.utils.RedisUtils;
import cc.efit.repository.LocalStorageRepository;
import cc.efit.utils.*;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallCustomerMapper;
import cc.efit.call.api.repository.CallCustomerRepository;
import cc.efit.call.biz.service.dto.CallCustomerDto;
import cc.efit.call.biz.service.dto.CallCustomerQueryCriteria;
import cc.efit.call.api.domain.CallCustomer;
import cc.efit.call.biz.service.CallCustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 客户名单表Service业务层处理
 * 
 * @author across
 * @date 2025-09-10
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CallCustomerServiceImpl implements CallCustomerService  {

    private final CallCustomerRepository callCustomerRepository;
    private final CallCustomerMapper callCustomerMapper;
    private final CallTaskRepository callTaskRepository;
    private final DialogueTemplateApi dialogueTemplateApi;
    private final CallCustomerBatchRepository callCustomerBatchRepository;
    private final LocalStorageRepository localStorageRepository;
    private final CallCustomerImportDetailRepository callCustomerImportDetailRepository;
    private final RedisUtils redisUtils;
    private final RabbitTemplate rabbitTemplate;
    @ImportDataExecutor
    private final ExecutorService importExecutorService;

    @Override
    public PageResult<CallCustomerDto> queryAll(CallCustomerQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallCustomer> page = callCustomerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callCustomerMapper::toDto));
    }

    @Override
    public List<CallCustomerDto> queryAll(CallCustomerQueryCriteria criteria){
        return callCustomerMapper.toDto(callCustomerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询客户名单表
     * 
     * @param id 客户名单表主键
     * @return 客户名单表
     */
    @Override
    public CallCustomerDto selectCallCustomerById(Integer id)  {
        return callCustomerMapper.toDto(callCustomerRepository.findById(id).orElseGet(CallCustomer::new));
    }


    /**
     * 新增客户名单表
     * 
     * @param callCustomer 客户名单表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallCustomer(CallCustomer callCustomer) {
        callCustomerRepository.save(callCustomer);
    }

    /**
     * 修改客户名单表
     * 
     * @param callCustomer 客户名单表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallCustomer(CallCustomer callCustomer) {
        callCustomerRepository.save(callCustomer);
    }

    /**
     * 批量删除客户名单表
     * 
     * @param ids 需要删除的客户名单表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallCustomerByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallCustomerById(id);
        }
    }

    /**
     * 删除客户名单表信息
     * 
     * @param id 客户名单表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallCustomerById(Integer id) {
        callCustomerRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallCustomerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallCustomerDto callCustomer : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callCustomer.getId());
            map.put("taskId",  callCustomer.getTaskId());
            map.put("phone",  callCustomer.getPhone());
            map.put("batchId",  callCustomer.getBatchId());
            map.put("batchNo",  callCustomer.getBatchNo());
            map.put("caseId",  callCustomer.getCaseId());
            map.put("status",  callCustomer.getStatus());
            map.put("calledStatus",  callCustomer.getCalledStatus());
            map.put("transferStatus",  callCustomer.getTransferStatus());
            map.put("customerInfo",  callCustomer.getCustomerInfo());
            map.put("appId",  callCustomer.getAppId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void importCustomer(ImportCustomerInfo importReq) {
        //处理导入号码的相关需求
        //如果是手动输入的，直接同步处理
        //如果是文件导入的，然后异步处理
        Integer taskId = importReq.taskId();
        CallTask task =  callTaskRepository.findById(taskId).orElseThrow(()->new BadRequestException("任务不存在"));
        CallCustomerBatch customerBatch = buildCallCustomerBatch(taskId, CustomerSourceEnum.PAGE.getCode());
        Integer callTemplateId = task.getCallTemplateId();
        //查询任务所需的模板字典
        List<String> dicts = dialogueTemplateApi.getCallTemplateCustomerDict(callTemplateId);
        if (ImportCustomerTypeEnum.EXCEL.getType().equals(importReq.importType())) {
            //创建批次 导入文件
            importExecutorService.execute(() -> buildCallTaskCustomerImport(customerBatch, importReq.fileId(), dicts));
        }else {
            //手动输入
            try {
                List<CallCustomer> list = new ArrayList<>();
                int importSize = importReq.customers().size();
                Set<String> exists = new HashSet<>();
                for (CustomerItemInfo item: importReq.customers()) {
                    String phone  = item.phone();
                    if (!RegexUtils.isMobile(phone)) {
                        buildCustomerFailInfo(item, customerBatch,"手机号格式不正确");
                        continue;
                    }
                    if (!exists.add(phone)) {
                        buildCustomerFailInfo(item, customerBatch,"手机号重复");
                    }
                    CallCustomer callCustomer = buildCallCustomer(item,  importReq.taskId(), dicts, customerBatch);
                    if (callCustomer != null) {
                        list.add(callCustomer);
                    }
                }
                int failSize = importSize - list.size();
                customerBatch.setCustomerCount(importSize);
                customerBatch.setSuccessCount(list.size());
                customerBatch.setFailCount(failSize);
                customerBatch.setStatus(failSize==0?CustomerBatchEnum.IMPORT_SUCCESS.getCode() :
                        failSize==importSize ? CustomerBatchEnum.IMPORT_FAIL.getCode():
                                CustomerBatchEnum.PART_IMPORT_SUCCESS.getCode());
                if (!list.isEmpty()) {
                    callCustomerRepository.saveAll(list);
                    //有客户导入 调用方法去进行加载数据拨打
                    startDispatchCallTask(taskId,task.getCallTemplateId());
                    task.setTodayCustomers(task.getTodayCustomers()+list.size());
                    task.setTotalCustomers(task.getTotalCustomers()+list.size());
                    callTaskRepository.save(task);
                }
            }catch (Exception e){
                log.error("导入客户失败",e);
                customerBatch.setStatus(CustomerBatchEnum.IMPORT_FAIL.getCode());
            }finally {
                //更新批次信息
                callCustomerBatchRepository.save(customerBatch);
            }

        }
    }
    @Override
    public void startDispatchCallTask(Integer taskId,Integer callTemplateId) {
        //开始调度任务
        //通过mq 发送，确保调度可以解耦成微服务
        //先判断一次该任务是不是再后台拨打中
        boolean alreadyStart = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
        if (!alreadyStart) {
            TemplateInitInfo initInfo = dialogueTemplateApi.initTemplateInfoToRedis(callTemplateId);
            if (initInfo!=null) {
                rabbitTemplate.convertAndSend(TemplateKeyConstants.CALL_TEMPLATE_INIT_QUEUE_KEY, JsonUtils.toJsonString(initInfo));
            }
            //不添加redis set 集合，让mq添加，有可能启动失败
            rabbitTemplate.convertAndSend(DispatchKeyConstants.DISPATCH_CALL_TASK_MQ_QUEUE_KEY, taskId);
        }else {
            log.info("taskId:{},任务已经在后台拨打中",taskId);
            //判断加载数据的线程是否存在，启动加载数据线程
            boolean alreadyLoad = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA, taskId);
            if (!alreadyLoad) {
                //启动加载数据线程,说明在拨打中，但是加载数据线程已经执行完了，直接在启动一个就行
                rabbitTemplate.convertAndSend(DispatchKeyConstants.DISPATCH_CALL_TASK_LOAD_DATA_MQ_QUEUE_KEY, taskId);
            }
        }
    }

    @Override
    public long countCustomerByStatusAndTaskId(CallCustomerStatusEnum.CustomerStatus customerStatus, Integer taskId) {
        return callCustomerRepository.countByTaskIdAndStatus(taskId, customerStatus.getStatus());
    }

    private CallCustomerBatch buildCallCustomerBatch(Integer taskId, int source) {
        CallCustomerBatch batch = new CallCustomerBatch();
        batch.setTaskId(taskId);
        batch.setSource(source);
        batch.setCustomerCount(0);
        batch.setSuccessCount(0);
        batch.setFailCount(0);
        batch.setBatchNo( SnowflakeUtils.nextId() );
        batch.setStatus(CustomerBatchEnum.NOT_IMPORT.getCode());
        callCustomerBatchRepository.save(batch);
        return batch;
    }

    private CallCustomer buildCallCustomer(CustomerItemInfo item,
                                           Integer taskId, List<String> dicts, CallCustomerBatch customerBatch) {
        //校验客户字典的数量是否够
        Map<String,String> customerInfo = null;
        if (CollectionUtils.isNotEmpty(dicts)) {
            //说明字典存在，需要校验数据是否符合格式
            customerInfo = buildCustomerInfo(item.customerInfo(), dicts);
            if (customerInfo==null) {
                //记录缺失的客户字段
                buildCustomerFailInfo(item, customerBatch,"客户字典缺失");
                return null;
            }
        }
        return buildCallCustomer(item.phone(), taskId, customerBatch, customerInfo,false);
    }

    private CallCustomer buildCallCustomer(String phone, Integer taskId,
                                           CallCustomerBatch customerBatch,
                                           Map<String, String> customerInfo,boolean async) {
        CallCustomer callCustomer = new CallCustomer();
        callCustomer.setTaskId(taskId);
        callCustomer.setBatchNo(customerBatch.getBatchNo());
        callCustomer.setCustomerInfo(customerInfo);
        callCustomer.setBatchId(customerBatch.getId());
        callCustomer.setPhone(phone);
        callCustomer.setStatus(CallCustomerStatusEnum.CustomerStatus.NOT_CALL.getStatus());
        callCustomer.setCallId(SnowflakeUtils.nextId());
        if (async) {
            //异步处理的时候，基础值保存进去
            callCustomer.setOrgId(customerBatch.getOrgId());
            callCustomer.setDeptId(customerBatch.getDeptId());
            callCustomer.setUserId(customerBatch.getUserId());
        }
        return callCustomer;
    }

    private void buildCustomerFailInfo(CustomerItemInfo item, CallCustomerBatch customerBatch, String failDesc) {
        log.info("手工上传客户信息校验失败，客户信息：{},批次号：{},失败原因：{}",item,customerBatch.getBatchNo(),failDesc);
        buildCustomerFailInfo(item.phone(),customerBatch,failDesc);
    }

    private void buildCustomerFailInfo(Map<String,String> customerInfo, CallCustomerBatch customerBatch, String failDesc) {
        log.info("excel导入客户信息校验失败，客户信息：{},批次号：{},失败原因：{}",customerInfo,customerBatch.getBatchNo(),failDesc);
        buildCustomerFailInfo(customerInfo.get(TemplateConstants.CUSTOMER_DICT_PHONE),customerBatch,failDesc);
    }

    private void buildCustomerFailInfo(String phone, CallCustomerBatch customerBatch, String failDesc) {
        CallCustomerImportDetail detail = new CallCustomerImportDetail();
        detail.setBatchId(customerBatch.getId());
        detail.setPhone(phone);
        detail.setReason(failDesc);
        detail.setBatchNo(customerBatch.getBatchNo());
        detail.setTaskId(customerBatch.getTaskId());
        detail.setOrgId(customerBatch.getOrgId());
        detail.setDeptId(customerBatch.getDeptId());
        detail.setUserId(customerBatch.getUserId());
        callCustomerImportDetailRepository.save(detail);
    }

    private Map<String, String> buildCustomerInfo(String customerInputInfo , List<String> dicts) {
        if (StringUtils.isBlank(customerInputInfo)) {
            //说明配置的字典为空
            return null;
        }
        String[] split = customerInputInfo.replaceAll("，",",").split(",",-1);
        if (split.length < dicts.size()) {
            //说明客户字典不够
            return null;
        }
        Map<String, String> customerInfo = new HashMap<>();
        for (int i = 0; i < dicts.size(); i++) {
            customerInfo.put(dicts.get(i), split[i]);
        }
        return customerInfo;
    }


    private void buildCallTaskCustomerImport(CallCustomerBatch customerBatch, Integer fileId, List<String> dicts) {
        log.info("开始导入客户信息，批次号：{},任务id:{},fileId:{}",customerBatch.getBatchNo(),customerBatch.getTaskId(), fileId);
        List<CallCustomer> customerList = new ArrayList<>();
        Integer taskId = customerBatch.getTaskId();
        LocalStorage localStorage = null;
        try {
            GlobalPermissionHolder.setIgnore(true);
            localStorage = localStorageRepository.findById(fileId).orElse(null);
            if (localStorage == null) {
                log.error("文件不存在，fileId:{}",fileId);
                return;
            }
            String filePath = localStorage.getPath();
            ExcelCustomerRowHandler excelCustomerRowHandler = new ExcelCustomerRowHandler();
            ExcelUtil.readBySax(filePath, 0, excelCustomerRowHandler);
            List<Map<String, String>> customerInfoList = excelCustomerRowHandler.getResult();
            if (customerInfoList.isEmpty()) {
                log.error("导入客户信息，文件为空，fileId:{}",fileId);
                return;
            }
            int successCount = 0;
            Set<String> exists = new HashSet<>();
            for (Map<String, String> customerInfo : customerInfoList) {
                CallCustomer callCustomer = buildImportCallCustomer(customerInfo, taskId,dicts,customerBatch);
                if (callCustomer == null) {
                    continue;
                }
                if (!RegexUtils.isMobile(callCustomer.getPhone())) {
                    buildCustomerFailInfo(customerInfo, customerBatch,"手机号格式不正确");
                    continue;
                }
                if (!exists.add(callCustomer.getPhone())) {
                    buildCustomerFailInfo(customerInfo, customerBatch,"手机号重复");
                }
                customerList.add(callCustomer);
                if (customerList.size() >= 1000) {
                    callCustomerRepository.saveAll(customerList);
                    successCount += customerList.size();
                    customerList.clear();
                }
            }
            if (!customerList.isEmpty()) {
                successCount += customerList.size();
                callCustomerRepository.saveAll(customerList);
            }
            int importSize = customerInfoList.size();
            int failSize = importSize - successCount;
            customerBatch.setCustomerCount(importSize);
            customerBatch.setSuccessCount(successCount);
            customerBatch.setFailCount(failSize);
            customerBatch.setStatus(failSize==0?CustomerBatchEnum.IMPORT_SUCCESS.getCode() :
                    failSize==importSize ? CustomerBatchEnum.IMPORT_FAIL.getCode():
                            CustomerBatchEnum.PART_IMPORT_SUCCESS.getCode());
            if (successCount>0) {
                Optional<CallTask> taskOptional =  callTaskRepository.findById(taskId);
                if (taskOptional.isPresent()) {
                    CallTask task = taskOptional.get();
                    task.setTodayCustomers(task.getTodayCustomers()+successCount);
                    task.setTotalCustomers(task.getTotalCustomers()+successCount);
                    callTaskRepository.save(task);
                    startDispatchCallTask(taskId, task.getCallTemplateId());
                }
            }
        }catch (Exception e) {
            log.error("导入客户信息",e);
            customerBatch.setStatus(CustomerBatchEnum.IMPORT_FAIL.getCode());
        }finally {
            if (localStorage!=null) {
                localStorage.setUsed(1);
                localStorageRepository.save(localStorage);
            }
            callCustomerBatchRepository.save(customerBatch);
            GlobalPermissionHolder.clear();
        }
    }
    private CallCustomer buildImportCallCustomer(Map<String, String> customerInfo,
                                           Integer taskId, List<String> dicts, CallCustomerBatch customerBatch) {
        //校验客户字典的数量是否够
        if (CollectionUtils.isNotEmpty(dicts)) {
            //说明字典存在，需要校验数据是否符合格式
            boolean valid = validCustomerInfo(customerInfo, dicts);
            if (!valid ) {
                //记录缺失的客户字段
                buildCustomerFailInfo(customerInfo, customerBatch,"客户字典缺失");
                return null;
            }
        }
        return buildCallCustomer( customerInfo.get(TemplateConstants.CUSTOMER_DICT_PHONE), taskId,
                customerBatch, customerInfo,true);
    }


    private boolean validCustomerInfo(Map<String, String> customerInfo, List<String> dicts) {
        if (customerInfo.isEmpty() || customerInfo.size()<dicts.size()) {
            //说明配置的字典为空
            return false;
        }
        for (String dict : dicts) {
            boolean contains = customerInfo.containsKey(dict);
            if (!contains) {
                return false;
            }
        }
        return true;
    }

}
