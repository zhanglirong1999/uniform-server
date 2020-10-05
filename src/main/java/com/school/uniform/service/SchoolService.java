package com.school.uniform.service;

import com.school.uniform.model.dao.entity.School;
import com.school.uniform.model.dto.post.SchoolAdd;
import com.school.uniform.model.dto.post.TagAdd;
import org.springframework.validation.annotation.Validated;

@Validated
public interface SchoolService {
    void addSchool(SchoolAdd schoolAdd);
    void putSchool(School school);
    void deleteSchool(Long schoolId);
    Object getSchoolList();
    Object getUserStudent(Long schoolId,String accountId);
}
