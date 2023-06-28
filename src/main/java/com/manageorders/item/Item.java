package com.manageorders.item;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
