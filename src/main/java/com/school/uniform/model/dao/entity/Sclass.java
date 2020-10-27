package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sclass {
    @Column(name = "schoolId")
    private Long schoolId;

    private String grade;

    private String classes;
}
