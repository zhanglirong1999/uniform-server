package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Sclass;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface SclassMapper extends Mapper<Sclass> {

    @Select("SELECT grade,classes from sclass where schoolId=${schoolId}")
    List<Map<String,Object>> getSchoolClass(Long schoolId);
}
