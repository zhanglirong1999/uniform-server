package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @Column(name = "studentId")
    private Long studentId;

    private String name;

    private Integer gender;
    @Column(name = "schoolNum")

    private String schoolNum;
    @Column(name = "schoolId")

    private Long schoolId;

    private String class1;

    private String height;

    private String weight;

    private String chest;

    private String waistline;

    private String hipline;

    @Column(name = "accountId")

    private String accountId;

    private String phone;

    private Integer deleted;



}