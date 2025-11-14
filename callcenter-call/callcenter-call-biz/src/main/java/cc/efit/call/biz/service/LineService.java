package cc.efit.call.biz.service;

import java.util.List;

import cc.efit.call.api.vo.line.LineInfo;
import cc.efit.call.api.domain.Line;
import cc.efit.call.biz.service.dto.LineDto;
import cc.efit.call.biz.service.dto.LineQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 中继线路网关表Service接口
 * 
 * @author across
 * @date 2025-08-27
 */
public interface LineService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<LineDto> queryAll(LineQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<lineDto>
    */
    List<LineDto> queryAll(LineQueryCriteria criteria);
    /**
     * 查询中继线路网关表
     * 
     * @param id 中继线路网关表主键
     * @return 中继线路网关表
     */
    LineDto selectLineById(Integer id);


    /**
     * 新增中继线路网关表
     * 
     * @param line 中继线路网关表
     */
    void insertLine(Line line);

    /**
     * 修改中继线路网关表
     * 
     * @param line 中继线路网关表
     */
    void updateLine(Line line);

    /**
     * 批量删除中继线路网关表
     * 
     * @param ids 需要删除的中继线路网关表主键集合
     */
    void deleteLineByIds(Integer[] ids);

    /**
     * 删除中继线路网关表信息
     * 
     * @param id 中继线路网关表主键
     */
    void deleteLineById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<LineDto> all, HttpServletResponse response) throws IOException;

    List<LineInfo> findLineAssignInfo();
}
