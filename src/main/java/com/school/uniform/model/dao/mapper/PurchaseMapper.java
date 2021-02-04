package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Purchase;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface PurchaseMapper extends Mapper<Purchase> {

    @Select("SELECT state from purchase where nonceStr='${nonceStr}'")
    String getStateByNonce(String nonceStr);

    @Update("UPDATE purchase set state='1' where nonceStr='${nonceStr}'")
    Integer updateState(String nonceStr);

    @Update("UPDATE purchase set nonceStr='${nonceStr}' where orderId=${orderId}")
    Integer updateNonceStr(String nonceStr,Long orderId);

    @Delete("DELETE FROM purchase WHERE orderId=${orderId}")
    Integer deletePurchase(Long orderId);

    @Delete("DELETE FROM purchase_map WHERE purId=${orderId}")
    Integer deleteMap(Long orderId);



}