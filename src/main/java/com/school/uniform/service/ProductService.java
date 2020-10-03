package com.school.uniform.service;

import com.school.uniform.model.dto.post.*;
import org.springframework.validation.annotation.Validated;

@Validated

public interface ProductService {
    Object getProductList();
    void addTag(TagAdd tagAdd);
    void deletePro(Long productId);
    void deleteTag(Long tagId);
    Object getTag();
    Object getSchoolList(Long schoolId);
    void productAdd(ProductAdd productAdd);
    void addShopping(ShoppingPost shopping, String accountId);
    Object productDetail(Long productId);
    void purchase(Purchase1 purchaseShop,String accountId);
    Object getCartList(String accountId);
    Object getPurchaseList(String accountId);
    Object searchProduct(String index);
    Object searchByTagAndSchool(String[] school,String[] tag);
    void sendProduct(Send send);
    void addSolicit(AddSolicite addSolicite);
}
