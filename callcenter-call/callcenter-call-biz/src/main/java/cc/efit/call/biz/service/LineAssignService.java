package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.api.domain.LineAssign;
import cc.efit.call.biz.service.dto.LineAssignDto;
import cc.efit.call.biz.service.dto.LineAssignQueryCriteria;
import cc.efit.call.biz.vo.line.AssignLineVo;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 线路分配表Service接口
 * 
 * @author across
 * @date 2025-08-28
 */
public interface LineAssignService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<LineAssignDto> queryAll(LineAssignQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<lineAssignDto>
    */
    List<LineAssignDto> queryAll(LineAssignQueryCriteria criteria);
    /**
     * 查询线路分配表
     * 
     * @param id 线路分配表主键
     * @return 线路分配表
     */
    LineAssignDto selectLineAssignById(Integer id);


    /**
     * 新增线路分配表
     * 
     * @param lineAssign 线路分配表
     */
    void insertLineAssign(LineAssign lineAssign);

    /**
     * 修改线路分配表
     * 
     * @param lineAssign 线路分配表
     */
    void updateLineAssign(LineAssign lineAssign);

    /**
     * 批量删除线路分配表
     * 
     * @param ids 需要删除的线路分配表主键集合
     */
    void deleteLineAssignByIds(Integer[] ids);

    /**
     * 删除线路分配表信息
     * 
     * @param id 线路分配表主键
     */
    void deleteLineAssignById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<LineAssignDto> all, HttpServletResponse response) throws IOException;

    void assignLineInfo(AssignLineVo vo);

    AssignLineVo getAssignLineInfo(Integer lineId);
}
