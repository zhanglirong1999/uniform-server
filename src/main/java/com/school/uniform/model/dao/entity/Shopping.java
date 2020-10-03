package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shopping {
    @Column(name = "shopId")
    private Long shopId;

    @Column(name = "productId")
    private Long productId;

    private String size;

    private Integer count;

    private Integer sex;

    @Column(name = "addTime")
    private Date addTime;

    @Column(name = "accountId")
    private String accountId;


}