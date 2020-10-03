package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Admin;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface AdminMapper extends Mapper<Admin> {
    @Select("SELECT * FROM admin where phone='${phone}'")
    Admin getOneAdminByPhone(String phone);
}