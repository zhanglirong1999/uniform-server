package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Solicitation;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SolicitationMapper extends Mapper<Solicitation> {
    @Select("SELECT schoolId from solicitation where sid =${sid}")
    Long getSchoolId(Long sid);

    @Select("SELECT type from solicitation where sid =${sid}")
    String getOnlineType(Long sid);
}