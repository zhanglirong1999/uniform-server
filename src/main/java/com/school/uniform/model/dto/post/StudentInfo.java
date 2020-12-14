package com.school.uniform.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo {
    private String name;

    private Integer gender;

    private String schoolNum;

    private String height;

    private String weight;

    private String chest;

    private String waistline;

    private String hipline;

    private String class1;

    private String phone;
}
