package com.manageorders.stockmovement;

import lombok.Data;

@Data
public class StockMovementDTO {
    private String itemName;
    private Long itemId;
    private Integer quantity;
}
