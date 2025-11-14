package cc.efit.call.biz.service;

import java.util.List;

import cc.efit.call.biz.domain.BlackInfo;
import cc.efit.call.biz.service.dto.BlackInfoDto;
import cc.efit.call.biz.service.dto.BlackInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 黑名单库表Service接口
 * 
 * @author across
 * @date 2025-08-27
 */
public interface BlackInfoService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<BlackInfoDto> queryAll(BlackInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<blackInfoDto>
    */
    List<BlackInfoDto> queryAll(BlackInfoQueryCriteria criteria);
    /**
     * 查询黑名单库表
     * 
     * @param id 黑名单库表主键
     * @return 黑名单库表
     */
    BlackInfoDto selectBlackInfoById(Integer id);


    /**
     * 新增黑名单库表
     * 
     * @param blackInfo 黑名单库表
     */
    void insertBlackInfo(BlackInfo blackInfo);

    /**
     * 修改黑名单库表
     * 
     * @param blackInfo 黑名单库表
     */
    void updateBlackInfo(BlackInfo blackInfo);

    /**
     * 批量删除黑名单库表
     * 
     * @param ids 需要删除的黑名单库表主键集合
     */
    void deleteBlackInfoByIds(Integer[] ids);

    /**
     * 删除黑名单库表信息
     * 
     * @param id 黑名单库表主键
     */
    void deleteBlackInfoById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<BlackInfoDto> all, HttpServletResponse response) throws IOException;
}
