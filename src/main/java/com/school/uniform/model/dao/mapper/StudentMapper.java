package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Student;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface StudentMapper extends Mapper<Student> {

    @Select("SELECT class1 from student where studentId=${studentId}")
    String getStudentClass(Long studentId);

    @Select("SELECT name from student where studentId=${studentId}")
    String getStudentName(Long studentId);

    @Select("SELECT schoolId from student where studentId=${studentId}")
    Long getSchoolId(Long studentId);

    @Select("SELECT * from student where studentId=${studentId}")
    Student getStudent(Long studentId);

    @Update("UPDATE student set deleted=1 where studentId=${studentId}")
    Integer deleteStudent(Long studentId);


}