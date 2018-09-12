package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.ItxiaMember;
import lombok.var;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItxiaMemberRepository extends JpaRepository<ItxiaMember, Integer> {

    /**
     * 根据登录名查询，即it侠用户的登陆账号
     *
     * @param loginName 登陆名
     * @return 返回查询结果
     */
    @Query("from ItxiaMember i where i.loginName = :loginName")
    List<ItxiaMember> findByLoginName(@Param("loginName") String loginName);

    /**
     * 根据登录名查询，即it侠用户的登陆账号
     * 此方法只返回一个结果
     *
     * @param loginName 登陆名
     * @return 返回查询结果
     */
    default ItxiaMember findOneByLoginName(String loginName) {
        var resultList = findByLoginName(loginName);
        return resultList.stream()
                .limit(1)
                .reduce((a, b) -> a)
                .orElse(null);
    }
}
