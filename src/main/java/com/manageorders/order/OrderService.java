package com.manageorders.order;

import com.manageorders.item.Item;
import com.manageorders.item.ItemRepository;
import com.manageorders.item.ItemService;
import com.manageorders.user.User;
import com.manageorders.user.UserRepository;
import com.manageorders.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository, ItemService itemService, UserService userService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.userService = userService;
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
        Long itemId = orderDTO.getItem().getId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Does not exists item"));

        Order order = new Order();
        order.setUser(user);
        order.setItem(item);
        order.setQuantity(orderDTO.getQuantity());
        order.setStatus("Pending");
        order.setCreationDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Optional<Order> optionalOrders = getOrderById(id);
        if (optionalOrders.isEmpty()) {
            throw new Error("Order does not exist");
        }

        Order order = new Order();
        order.setId(optionalOrders.get().getId());

        Optional<Item> optionalItem = itemService.getItemById(orderDTO.getItem().getId());
        Item item = new Item();
        item.setId(optionalItem.get().getId());
        item.setName(optionalItem.get().getName());
        order.setItem(item);

        Optional<User> optionalUser = userService.getUserById(orderDTO.getUser().getId());
        User user = new User();
        user.setId(optionalUser.get().getId());
        user.setName(optionalUser.get().getName());
        order.setUser(user);

        order.setQuantity(orderDTO.getQuantity());
        order.setCreationDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
