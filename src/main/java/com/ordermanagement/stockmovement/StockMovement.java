package com.ordermanagement.stockmovement;

import com.ordermanagement.item.Item;
import com.ordermanagement.stock.Stock;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "stock_movements")
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Item item;
    private Integer quantity;
    private LocalDateTime creationDate;
    @ManyToOne
    private Stock stock;
}
