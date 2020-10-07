package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Account;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface AccountMapper extends Mapper<Account> {
    @Select("SELECT openId from account where accountId ='${id}'")
    String getOpenId(String id);

}