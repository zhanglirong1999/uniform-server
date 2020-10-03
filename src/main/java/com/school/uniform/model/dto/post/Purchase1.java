package com.school.uniform.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase1 {
    PurchaseShop[] purchaseShops;
    Long positionId;
    String type;
    String form;
    String state;
}
