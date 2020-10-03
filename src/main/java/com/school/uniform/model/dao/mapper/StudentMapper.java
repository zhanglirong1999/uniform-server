package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Student;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface StudentMapper extends Mapper<Student> {

}