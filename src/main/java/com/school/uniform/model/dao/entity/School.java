package com.school.uniform.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.LogicDelete;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class School {
    @Column(name = "schoolId")
    private Long schoolId;

    private String name;

    private String description;

    @LogicDelete(notDeletedValue = 0,isDeletedValue = 1)
    private Integer deleted;
}