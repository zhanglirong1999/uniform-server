package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.LogicDelete;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @Column(name = "productId")
    private Long productId;
    @Column(name = "productName")

    private String productName;
    @Column(name = "schoolId")

    private Long schoolId;

    private String description;

    private String freight;

    private String type;

    private String img;

    private String price;

    private Integer sex;

    @LogicDelete(notDeletedValue = 0,isDeletedValue = 1)
    private Integer deleted;
}