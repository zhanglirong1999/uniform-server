package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitation {
    @Id
    private Long sid;
    @Column(name = "schoolId")
    private Long schoolId;

    private String description;

    private String type;


}