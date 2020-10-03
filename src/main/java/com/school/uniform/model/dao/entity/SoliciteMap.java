package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoliciteMap {
    @Id
    private Integer id;

    @Column(name = "solicitId")
    private Long solicitId;

    @Column(name = "productId")
    private Long productId;

    private Integer count;

}