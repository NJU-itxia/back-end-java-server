package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.LoginRequest;
import lombok.var;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yzh
 */
public interface LoginRequestRepository extends JpaRepository<LoginRequest, Long>, JpaSpecificationExecutor<LoginRequest> {

    /**
     * 查询某个时间后的列表
     *
     * @param phone 手机号
     * @param time  时间
     * @return 查询结果
     */
    @Query("from LoginRequest l where l.phone = :phone and l.sendTime > :time")
    List<LoginRequest> findAfterTimeByPhone(@Param("phone") String phone, @Param("time") LocalDateTime time);

    /**
     * 查询一个符合条件的登陆请求
     *
     * @param phone 手机号
     * @param time  时间
     * @return 查询结果
     */
    default LoginRequest findOneAfterTimeByPhone(String phone, LocalDateTime time) {
        var resultList = findAfterTimeByPhone(phone, time);
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
