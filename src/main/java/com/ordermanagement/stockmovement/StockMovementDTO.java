package com.ordermanagement.stockmovement;

import lombok.Data;

@Data
public class StockMovementDTO {
    private Long itemId;
    private Long stockId;
    private String stockOperation;
    private Integer quantity;
}
