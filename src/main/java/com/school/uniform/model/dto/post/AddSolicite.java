package com.school.uniform.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSolicite {
    Long schoolId;
    String description;
    Long[] productId;
    Integer[] count;
    String type;
}
