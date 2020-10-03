package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Purchase;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface PurchaseMapper extends Mapper<Purchase> {

}