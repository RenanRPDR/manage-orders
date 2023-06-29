package com.ordermanagement.order;

import com.ordermanagement.item.Item;
import com.ordermanagement.user.User;
import lombok.Data;

@Data
public class OrderDTO {
    private Item item;
    private Integer quantity;
    private User user;
}
