package com.school.uniform.service;

import com.school.uniform.model.dto.post.Register;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AccountService {
    void registerUser(Register register,String accountId);
    Object getUserInfo(String accountId);
    Object getStudentList(String accountId);
}
