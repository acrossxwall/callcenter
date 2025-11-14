package cc.efit.call.api.repository;

import cc.efit.call.api.vo.line.DispatchLineVo;
import cc.efit.call.api.vo.task.CallStatusCountInfo;
import cc.efit.call.api.vo.task.CallTaskConcurrency;
import cc.efit.call.api.vo.task.SystemTaskInfo;
import cc.efit.db.base.LogicDeletedRepository;
import cc.efit.call.api.vo.task.CallTaskInfo;
import cc.efit.call.api.domain.CallTask;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 呼叫任务表Repository接口
 * 
 * @author across
 * @date 2025-08-27
 */
public interface CallTaskRepository extends LogicDeletedRepository<CallTask, Integer>, JpaSpecificationExecutor<CallTask> {

    CallTask findByTaskName(String taskName);
    @Query("update CallTask t set t.status = ?2 where t.id = ?1")
    @Modifying
    void updateStatusById(Integer id, Integer status);

    @Query("update CallTask t set t.callStatus = ?2 where t.id = ?1")
    @Modifying
    void updateCallStatusById(Integer id, Integer callStatus);

    @Query("""
        select new cc.efit.call.api.vo.task.CallTaskInfo(t.id,t.callTemplateId,t.taskName)
        from CallTask t where t.status = ?1
        """)
    List<CallTaskInfo> selectAllTaskByStatus(Integer status);
    @Query("""
            select
                 new cc.efit.call.api.vo.task.CallTaskConcurrency(t.id,t.lineConcurrent,t.priority)
                 from CallTask t where t.status =  1 and t.callStatus=1 and t.lineId = ?1 and t.deptId = ?2
            """)
    List<CallTaskConcurrency> selectRunningTaskByStatus(Integer lineId,Integer deptId);
    @Query("""
            select new cc.efit.call.api.vo.line.DispatchLineVo(t.lineId,t.deptId)
            from CallTask t where t.status = 1 and t.callStatus=1
        """)
    List<DispatchLineVo> selectRunningDispatchLineVo ();
    @Query("""
            update CallTask t set t.todayCalled = ?1,t.todayConnect=?2 where t.id = ?3
            """)
    @Modifying
    void updateCallTaskStatisticsInfo(Integer callCount,Integer connectCount,Integer id);

    @Query("SELECT  ct.callStatus, COUNT(1) as count FROM CallTask ct GROUP BY ct.callStatus")
    List<CallStatusCountInfo> countByStatus();
    @Query("""
            select new cc.efit.call.api.vo.task.SystemTaskInfo(t.id,t.orgId,t.deptId,t.userId)
            from CallTask t where t.status = 1 and t.callStatus=1
        """)
    List<SystemTaskInfo> selectRunningSystemTask();
}
