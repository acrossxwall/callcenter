package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.CallTemplateOperatorMapper;
import cc.efit.dialogue.biz.repository.CallTemplateOperatorRepository;
import cc.efit.dialogue.biz.service.dto.CallTemplateOperatorDto;
import cc.efit.dialogue.biz.service.dto.CallTemplateOperatorQueryCriteria;
import cc.efit.dialogue.biz.domain.CallTemplateOperator;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 话术模板操作日志Service业务层处理
 * 
 * @author across
 * @date 2025-08-12
 */
@Service
@RequiredArgsConstructor
public class CallTemplateOperatorServiceImpl implements CallTemplateOperatorService  {

    private final CallTemplateOperatorRepository callTemplateOperatorRepository;
    private final CallTemplateOperatorMapper callTemplateOperatorMapper;

    @Override
    public PageResult<CallTemplateOperatorDto> queryAll(CallTemplateOperatorQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallTemplateOperator> page = callTemplateOperatorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callTemplateOperatorMapper::toDto));
    }

    @Override
    public List<CallTemplateOperatorDto> queryAll(CallTemplateOperatorQueryCriteria criteria){
        return callTemplateOperatorMapper.toDto(callTemplateOperatorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询话术模板操作日志
     * 
     * @param id 话术模板操作日志主键
     * @return 话术模板操作日志
     */
    @Override
    public CallTemplateOperatorDto selectCallTemplateOperatorById(Integer id)  {
        return callTemplateOperatorMapper.toDto(callTemplateOperatorRepository.findById(id).orElseGet(CallTemplateOperator::new));
    }


    /**
     * 新增话术模板操作日志
     * 
     * @param callTemplateOperator 话术模板操作日志
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallTemplateOperator(CallTemplateOperator callTemplateOperator) {
        callTemplateOperatorRepository.save(callTemplateOperator);
    }

    /**
     * 修改话术模板操作日志
     * 
     * @param callTemplateOperator 话术模板操作日志
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallTemplateOperator(CallTemplateOperator callTemplateOperator) {
        callTemplateOperatorRepository.save(callTemplateOperator);
    }

    /**
     * 批量删除话术模板操作日志
     * 
     * @param ids 需要删除的话术模板操作日志主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTemplateOperatorByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallTemplateOperatorById(id);
        }
    }

    /**
     * 删除话术模板操作日志信息
     * 
     * @param id 话术模板操作日志主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTemplateOperatorById(Integer id) {
        callTemplateOperatorRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallTemplateOperatorDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallTemplateOperatorDto callTemplateOperator : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callTemplateOperator.getId());
            map.put("deptId",  callTemplateOperator.getDeptId());
            map.put("userId",  callTemplateOperator.getUserId());
            map.put("callTemplateId",  callTemplateOperator.getCallTemplateId());
            map.put("type",  callTemplateOperator.getType());
            map.put("content",  callTemplateOperator.getContent());
            map.put("ip",  callTemplateOperator.getIp());
            map.put("orgId",  callTemplateOperator.getOrgId());
            map.put("createBy",  callTemplateOperator.getCreateBy());
            map.put("updateBy",  callTemplateOperator.getUpdateBy());
            map.put("createTime",  callTemplateOperator.getCreateTime());
            map.put("deleted",  callTemplateOperator.getDeleted());
            map.put("updateTime",  callTemplateOperator.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
    @Override
    public void buildCallTemplateOperatorLog(Integer callTemplateId, Integer type, String content) {
        CallTemplateOperator log = new CallTemplateOperator ();
        log.setCallTemplateId(callTemplateId);
        log.setType(type);
        log.setContent(content);
        callTemplateOperatorRepository.save(log);
    }

}
