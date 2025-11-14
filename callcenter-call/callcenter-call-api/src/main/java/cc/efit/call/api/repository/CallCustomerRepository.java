package cc.efit.call.api.repository;

import cc.efit.call.api.domain.CallCustomer;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 客户名单表Repository接口
 * 
 * @author across
 * @date 2025-09-10
 */
public interface CallCustomerRepository extends LogicDeletedRepository<CallCustomer, Integer>, JpaSpecificationExecutor<CallCustomer> {

    List<CallCustomer> findByTaskIdAndStatus(Integer taskId, Integer status, Limit limit);

    @Modifying
    @Query("update CallCustomer set status = ?2 where id in ?1")
    void updateStatusByIds(List<Integer> ids, Integer status);
    @Modifying
    @Query("update CallCustomer set calledStatus = ?2,status = ?3 where id = ?1")
    void updateCustomerCallStatus(Integer id, Integer calledStatus,Integer status);

    @Modifying
    @Query("update CallCustomer set status = ?3 where taskId=?1 and status = ?2")
    void updateStatusByStatusAndTaskId( Integer taskId, Integer status,Integer targetStatus);

    long countByTaskIdAndStatus(Integer taskId, Integer status);
}
