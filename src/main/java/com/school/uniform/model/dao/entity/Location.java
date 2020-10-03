package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @Column(name = "locationId")
    private Long locationId;

    @Column(name = "accountId")
    private String accountId;

    private String position;

    private String name;

    private String phone;


}