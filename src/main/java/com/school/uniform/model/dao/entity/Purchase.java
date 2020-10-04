package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Column(name = "orderId")
    private Long orderId;

    private String state;

    private String number;
    @Column(name = "accountId")

    private String accountId;

    private String type;
    @Column(name = "buyTime")

    private Date buyTime;

    private String form;
    @Column(name = "positionId")

    private Long positionId;

    private String total;

    @Column(name = "schoolId")
    private Long schoolId;


}