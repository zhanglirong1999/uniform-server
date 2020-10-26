package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Price;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import org.springframework.stereotype.Repository;
@Repository
public interface PriceMapper extends Mapper<Price> {

    @Update("UPDATE price set count=(count-1) where id=${id}")
    Integer updateCount(Long id);

}