package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.School;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface SchoolMapper extends Mapper<School> {

    @Select("SELECT schoolId from school where name='${name}'")
    Long getSchoolId(String name);

    @Select("SELECT name from school where schoolId =${schoolId}")
        String getSchoolName(long schoolId);


}