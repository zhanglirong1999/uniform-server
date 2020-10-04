package com.school.uniform.service;

import com.school.uniform.model.dto.post.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

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
    Object purchase(Purchase1 purchaseShop,String accountId);
    Object getCartList(String accountId);
    Object getPurchaseList(String accountId);
    Object searchProduct(String index);
    Object searchByTagAndSchool(String[] school,String[] tag);
    void sendProduct(Send send);
    void addSolicit(AddSolicite addSolicite);
    String uploadFile(MultipartFile file);
    String getFileUrl(Long productId);
    Object getSearchBy(Long schoolId ,String state,String type);
    Object hasBuy(Long orderId);
}
