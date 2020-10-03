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
public class Account {
    @Id
    @Column(name = "accountId")
    private String accountId;
    @Column(name = "openId")
    private String openId;

    private String wechat;

    private String name;

    private String avatar;
    @Column(name = "lastTime")
    private Date lastTime;


}