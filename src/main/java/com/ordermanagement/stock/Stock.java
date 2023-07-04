package com.ordermanagement.stock;

import com.ordermanagement.item.Item;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Item item;
    private Integer quantity;
    private LocalDateTime creationDate;
}
