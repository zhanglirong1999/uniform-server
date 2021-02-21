package com.school.uniform.model.dao.mapper;

import com.school.uniform.model.dao.entity.Price;
import com.school.uniform.model.dao.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface ProductMapper extends Mapper<Product> {
    @Select("SELECT id from price where productId=${productId} AND size='${size}'")
    Long getPriceId(Long productId,String size);

    @Select("SELECT * FROM product where deleted=0 AND productName LIKE '%${index}%'")
    List<Product> search(String index);

    @Select("SELECT * FROM product where type like '%${index}%'")
    List<Product> searchByTag(String index);

    @Select("SELECT product.* FROM product left join school s on product.schoolId = s.schoolId where s.name like '%${index}%'")
    List<Product> searchBySchool(String index);

    @Select("SELECT freight FROM product where productId =${productId}")
    String getFreight(Long productId);

    @Select("SELECT productName FROM product where productId =${productId}")
    String getProductName(Long productId);

    @Select("SELECT img FROM product where productId =${productId}")
    String getProductImg(Long productId);

    @Select("SELECT * FROM price where productId =${productId} and size='${size}'")
    Price getProductByIdAndSize(Long productId, String size);

    @Select("select count(*) from purchase_map left join purchase p on purchase_map.purId = p.orderId where p.accountId='${accountId}' " +
            "and p.state=1 " +
            "and purchase_map.productId=${productId}")
    Integer ifHasPurchase(String accountId,Long productId);

}