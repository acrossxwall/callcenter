package cc.efit.call.biz.service.impl;

import cc.efit.call.api.domain.Line;
import cc.efit.call.api.repository.LineRepository;
import cc.efit.call.biz.service.dto.LineAssignDto;
import cc.efit.call.biz.service.mapstruct.LineAssignMapper;
import cc.efit.call.biz.vo.line.AssignLineItemInfo;
import cc.efit.call.biz.vo.line.AssignLineVo;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.api.repository.LineAssignRepository;
import cc.efit.call.biz.service.dto.LineAssignQueryCriteria;
import cc.efit.call.api.domain.LineAssign;
import cc.efit.call.biz.service.LineAssignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.util.*;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * 线路分配表Service业务层处理
 * 
 * @author across
 * @date 2025-08-28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LineAssignServiceImpl implements LineAssignService {

    private final LineAssignRepository lineAssignRepository;
    private final LineAssignMapper lineAssignMapper;
    private final LineRepository lineRepository;
    @Override
    public PageResult<LineAssignDto> queryAll(LineAssignQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<LineAssign> page = lineAssignRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(lineAssignMapper::toDto));
    }

    @Override
    public List<LineAssignDto> queryAll(LineAssignQueryCriteria criteria){
        return lineAssignMapper.toDto(lineAssignRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询线路分配表
     * 
     * @param id 线路分配表主键
     * @return 线路分配表
     */
    @Override
    public LineAssignDto selectLineAssignById(Integer id)  {
        return lineAssignMapper.toDto(lineAssignRepository.findById(id).orElseGet(LineAssign::new));
    }


    /**
     * 新增线路分配表
     * 
     * @param lineAssign 线路分配表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertLineAssign(LineAssign lineAssign) {
        lineAssignRepository.save(lineAssign);
    }

    /**
     * 修改线路分配表
     * 
     * @param lineAssign 线路分配表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLineAssign(LineAssign lineAssign) {
        lineAssignRepository.save(lineAssign);
    }

    /**
     * 批量删除线路分配表
     * 
     * @param ids 需要删除的线路分配表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLineAssignByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteLineAssignById(id);
        }
    }

    /**
     * 删除线路分配表信息
     * 
     * @param id 线路分配表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLineAssignById(Integer id) {
        lineAssignRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<LineAssignDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LineAssignDto lineAssign : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  lineAssign.getId());
            map.put("lineId",  lineAssign.getLineId());
            map.put("assignDeptId",  lineAssign.getAssignDeptId());
            map.put("concurrency",  lineAssign.getConcurrency());
            map.put("createBy",  lineAssign.getCreateBy());
            map.put("createTime",  lineAssign.getCreateTime());
            map.put("updateBy",  lineAssign.getUpdateBy());
            map.put("updateTime",  lineAssign.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignLineInfo(AssignLineVo vo) {
        log.info("收到分配线路并发的请求:{}",vo);
        Integer lineId = vo.id();
        if (lineId==null || CollectionUtils.isEmpty(vo.assignInfo())) {
            throw new BadRequestException("参数错误");
        }
        Line line = lineRepository.findById(lineId).orElseThrow(()->new BadRequestException("线路不存在"));
        if (YesNoEnum.NO.getCode().equals(line.getStatus())) {
            throw new BadRequestException("线路已停用");
        }
        int sum = vo.assignInfo().stream().mapToInt(AssignLineItemInfo::concurrency).sum();
        if (sum > line.getConcurrency()) {
            throw new BadRequestException("分配并发数不能超过线路总并发数");
        }
        //查找历史分配的
        List<LineAssign> list = lineAssignRepository.findByLineId(lineId);
        Set<Integer> currentAssign = vo.assignInfo().stream().map(AssignLineItemInfo::deptId).collect(Collectors.toSet());
        List<Integer> deleted = null;
        if (!CollectionUtils.isEmpty(list)) {
            deleted = list.stream().filter(item->!currentAssign.contains(item.getAssignDeptId())).map(LineAssign::getId).toList();
            if (!CollectionUtils.isEmpty(deleted)) {
                lineAssignRepository.deleteByLineIdAndAssignDeptIdIn(lineId,deleted);
            }
        }
        vo.assignInfo().forEach(item-> buildDeptAssignLine(item, lineId));
    }

    @Override
    public AssignLineVo getAssignLineInfo(Integer lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(()->new BadRequestException("线路不存在"));
        List<LineAssign> list = lineAssignRepository.findByLineId(lineId);
        return new AssignLineVo(lineId,line.getConcurrency() , list==null?null:list.stream().map(item->new AssignLineItemInfo(item.getAssignDeptId(), item.getConcurrency())).toList());
    }

    private void buildDeptAssignLine(AssignLineItemInfo item, Integer lineId) {
        Integer deptId = item.deptId();
        LineAssign lineAssign = lineAssignRepository.findByLineIdAndAssignDeptId(lineId, deptId);
        if (lineAssign!=null && lineAssign.getConcurrency().equals(item.concurrency())) {
            //分配并发没有修改， do noting
            return;
        }
        if (lineAssign == null) {
            //新增
            lineAssign = new LineAssign();
            lineAssign.setLineId(lineId);
            lineAssign.setAssignDeptId(deptId);
            lineAssign.setConcurrency(item.concurrency());
        }else {
            lineAssign.setConcurrency(item.concurrency());
        }
        lineAssignRepository.save(lineAssign);
    }

}
