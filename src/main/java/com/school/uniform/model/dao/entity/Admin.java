package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @Column(name = "accountId")
    private String accountId;

    private String phone;

    private String name;
    @Column(name = "cTime")
    private Date cTime;
    @Column(name = "uTime")
    private Date uTime;


}