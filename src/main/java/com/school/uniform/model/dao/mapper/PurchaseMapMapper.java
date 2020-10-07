package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.PurchaseMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface PurchaseMapMapper extends Mapper<PurchaseMap> {


}