package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseMap {
    @Column(name = "purId")
    private Long purId;
    @Column(name = "productId")
    private Long productId;

    private String size;

    private Integer count;

    private String price;

    private Integer sex;


}