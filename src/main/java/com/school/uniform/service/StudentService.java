package com.school.uniform.service;

import com.school.uniform.model.dto.post.PutStudent;
import com.school.uniform.model.dto.post.StudentInfo;
import org.springframework.validation.annotation.Validated;

@Validated
public interface StudentService {
    void addStudent(StudentInfo studentInfo,String accountId);
    Object getStudent(Long studentId);
    void putStudent(PutStudent putStudent,String accountId);
    void deleteStudent(Long studentId,String accountId);
}
