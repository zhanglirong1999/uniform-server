package com.school.uniform.service;

import com.school.uniform.model.dto.post.LocationChange;
import com.school.uniform.model.dto.post.LocationPost;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LocationService {
    void addPosition(LocationPost position, String accountId);
    void postPosition(LocationChange positon, String accountId);
    void deletePosition(Long positionId,String accountId);
    Object getPositionList(String accountId);
}
