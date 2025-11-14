package cc.efit.call.biz.service.impl;

import cc.efit.call.api.vo.line.CallLineInfo;
import cc.efit.call.api.vo.line.LineInfo;
import cc.efit.call.biz.service.dto.LineDto;
import cc.efit.enums.CommonStatusEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.LineMapper;
import cc.efit.call.api.repository.LineRepository;
import cc.efit.call.biz.service.dto.LineQueryCriteria;
import cc.efit.call.api.domain.Line;
import cc.efit.call.biz.service.LineService;
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

import static cc.efit.call.api.constants.DispatchKeyConstants.DISPATCH_LINE_KEY;

/**
 * 中继线路网关表Service业务层处理
 * 
 * @author across
 * @date 2025-08-27
 */
@Service
@RequiredArgsConstructor
public class LineServiceImpl implements LineService {

    private final LineRepository lineRepository;
    private final LineMapper lineMapper;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<LineDto> queryAll(LineQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<Line> page = lineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(lineMapper::toDto));
    }

    @Override
    public List<LineDto> queryAll(LineQueryCriteria criteria){
        return lineMapper.toDto(lineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询中继线路网关表
     * 
     * @param id 中继线路网关表主键
     * @return 中继线路网关表
     */
    @Override
    public LineDto selectLineById(Integer id)  {
        return lineMapper.toDto(lineRepository.findById(id).orElseGet(Line::new));
    }


    /**
     * 新增中继线路网关表
     * 
     * @param line 中继线路网关表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertLine(Line line) {
        validLineName(line);
        lineRepository.save(line);
        initLineRedis(line);
    }

    /**
     * 修改中继线路网关表
     * 
     * @param line 中继线路网关表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLine(Line line) {
        validLineName(line);
        Line dbLine = lineRepository.findById(line.getId()).orElseThrow(()-> new BadRequestException("记录不存在"));
        BeanUtils.copyProperties(line,dbLine);
        lineRepository.save(dbLine);
        initLineRedis(dbLine);
    }

    /**
     * 批量删除中继线路网关表
     * 
     * @param ids 需要删除的中继线路网关表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLineByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteLineById(id);
        }
    }

    /**
     * 删除中继线路网关表信息
     * 
     * @param id 中继线路网关表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLineById(Integer id) {
        lineRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<LineDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LineDto line : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  line.getId());
            map.put("realm",  line.getRealm());
            map.put("port",  line.getPort());
            map.put("username",  line.getUsername());
            map.put("password",  line.getPassword());
            map.put("register",  line.getRegister());
            map.put("createBy",  line.getCreateBy());
            map.put("createTime",  line.getCreateTime());
            map.put("updateBy",  line.getUpdateBy());
            map.put("updateTime",  line.getUpdateTime());
            map.put("deleted",  line.getDeleted());
            map.put("gatewayName",  line.getGatewayName());
            map.put("regStatus",  line.getRegStatus());
            map.put("lineName",  line.getLineName());
            map.put("callNumber",  line.getCallNumber());
            map.put("concurrency",  line.getConcurrency());
            map.put("unitPrice",  line.getUnitPrice());
            map.put("status",  line.getStatus());
            map.put("callPrefix",  line.getCallPrefix());
            map.put("remark",  line.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<LineInfo> findLineAssignInfo() {
        return lineRepository.selectAssignLineInfo();
    }

    private void validLineName(Line line) {
        String lineName = line.getLineName();
        if (StringUtils.isEmpty(lineName)) {
            throw new BadRequestException("线路名称不能为空");
        }
        Line exists = lineRepository.findByLineName(lineName);
        if (exists==null) {
            return;
        }
        if (line.getId()==null || !exists.getId().equals(line.getId())) {
            throw new BadRequestException("线路名称已存在");
        }
    }


    private void initLineRedis(Line line) {
        if (CommonStatusEnum.ENABLE.getCode().equals(line.getStatus())) {
            CallLineInfo lineInfo = new CallLineInfo(
                    line.getId(),line.getRealm(),line.getPort(),line.getCallNumber(),
                    line.getCallPrefix(),line.getLineName(),line.getGatewayName()
            );
            redisUtils.set(DISPATCH_LINE_KEY.formatted(line.getId()), JsonUtils.toJsonString( lineInfo));
        }
    }

}
