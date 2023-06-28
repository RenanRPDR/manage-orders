package com.manageorders.order;

import com.manageorders.item.Item;
import com.manageorders.user.User;
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

