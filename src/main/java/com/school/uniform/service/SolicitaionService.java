package com.school.uniform.service;

import com.school.uniform.model.dto.post.AddSolicite;
import com.school.uniform.model.dto.post.PostSolicit;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Validated
public interface SolicitaionService {

    void addSolicit(AddSolicite solicit);
    void deleteSolicit(Long sid);
    void postSolicit(PostSolicit solicit);
    Object getList();
    List getProduct(Long sid);
    Object getUserSolicit(Long sids);
    Object getSolicitProduct(Long sid);
}
