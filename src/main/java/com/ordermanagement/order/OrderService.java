package com.ordermanagement.order;

import com.ordermanagement.email.EmailService;
import com.ordermanagement.item.Item;
import com.ordermanagement.item.ItemRepository;
import com.ordermanagement.item.ItemService;
import com.ordermanagement.stockmovement.StockMovement;
import com.ordermanagement.stockmovement.StockMovementService;
import com.ordermanagement.user.User;
import com.ordermanagement.user.UserRepository;
import com.ordermanagement.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final StockMovementService stockMovementService;
    private final EmailService emailService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository, ItemService itemService, UserService userService, StockMovementService stockMovementService, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.stockMovementService = stockMovementService;
        this.emailService = emailService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Item item = itemRepository.findById(orderDTO.getItem().getId())
                .orElseThrow(() -> new IllegalArgumentException("Does not exists item"));

        String itemName = item.getName();
        Order order = new Order();
        if(stockMovementService.existsStockMovementByItemName(itemName)) {
            StockMovement stockMovement = stockMovementService.findStockMovementByItemName(itemName);

            if (stockMovement.getQuantity() >= orderDTO.getQuantity()) {
                order.setStatus("Done");
                Integer updateStockMovementQuantity = stockMovement.getQuantity() - orderDTO.getQuantity();
                stockMovement.setQuantity(updateStockMovementQuantity);
                System.out.println(emailService.sendSuccessOrder(user));
            }

            if (stockMovement.getQuantity() < orderDTO.getQuantity()) {
                order.setStatus("Pending");
                order.setQuantity(orderDTO.getQuantity());
            }

            order.setUser(user);
            order.setItem(item);
            order.setQuantity(orderDTO.getQuantity());
            order.setCreationDate(LocalDateTime.now());
        } else {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Optional<Order> optionalOrders = getOrderById(id);
        if (optionalOrders.isEmpty()) {
            throw new Error("Order does not exist");
        }

        Order order = new Order();
        Optional<Item> optionalItem = itemService.getItemById(orderDTO.getItem().getId());
        Item item = new Item();
        item.setId(optionalItem.get().getId());
        item.setName(optionalItem.get().getName());
        order.setItem(item);

        Optional<User> optionalUser = userService.getUserById(orderDTO.getUser().getId());
        User user = new User();
        user.setId(optionalUser.get().getId());
        user.setName(optionalUser.get().getName());

        order.setId(optionalOrders.get().getId());
        order.setUser(user);
        order.setStatus(optionalOrders.get().getStatus());
        order.setQuantity(orderDTO.getQuantity());
        order.setCreationDate(LocalDateTime.now());
        order.setCreationDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findOrderByItemId(Long itemId) {
        return orderRepository.findOrderByItemId(itemId);
    }
}
