package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    @Id
    private Long id;
    @Column(name = "productId")
    private Long productId;

    private String price;

    private String size;

}