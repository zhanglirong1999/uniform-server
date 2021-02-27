package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Location;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface LocationMapper extends Mapper<Location> {
    @Select("select count(*) from purchase where positionId=${id}")
    Integer getLocationCount(Long id);

}