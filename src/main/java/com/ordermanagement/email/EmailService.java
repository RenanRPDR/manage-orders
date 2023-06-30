package com.ordermanagement.email;

import com.ordermanagement.user.User;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public String sendSuccessOrder(User user) {
        return "Mr. " + user.getName() + ", your order has been fulfilled.";
    }
}
