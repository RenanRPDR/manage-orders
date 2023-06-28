package com.manageorders.order;

import com.manageorders.item.Item;
import com.manageorders.user.User;
import lombok.Data;

@Data
public class OrderDTO {
    private Item item;
    private Integer quantity;
    private User user;
}
