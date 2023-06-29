package com.ordermanagement.order;

import com.ordermanagement.item.Item;
import com.ordermanagement.user.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Item item;
    private Integer quantity;
    @ManyToOne
    private User user;
    private String status;
    private LocalDateTime creationDate;
}

