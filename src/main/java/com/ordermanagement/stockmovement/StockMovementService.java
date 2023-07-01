package com.ordermanagement.stockmovement;

import com.ordermanagement.item.Item;
import com.ordermanagement.item.ItemService;
import com.ordermanagement.order.Order;
import com.ordermanagement.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockMovementService implements IStockMovementService  {

    private final ItemService itemService;
    private final OrderRepository orderRepository;

    @Autowired
    private final IStockMovementRepository stockMovementRepository;

    public StockMovementService(IStockMovementRepository stockMovementRepository, ItemService itemService, OrderRepository orderRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemService = itemService;
        this.orderRepository = orderRepository;
    }

    @Override
    public StockMovement createStockMovement(StockMovementDTO stockMovementDTO) {
        Long itemId = stockMovementDTO.getItemId();
        Optional<Item> optionalItem = itemService.getItemById(itemId);
        if (optionalItem.isEmpty()){
            throw new Error("Item does not exist");
        }

        Boolean stockMovementForThisItem = existsStockMovementByItemName(optionalItem.get().getName());
        if (stockMovementForThisItem){
            throw new Error("Does exist stockmovement for this item");
        }

        StockMovement stockMovement = new StockMovement();
        if (!stockMovementForThisItem) {
            Item item = new Item();
            item.setId(optionalItem.get().getId());
            item.setName(optionalItem.get().getName());

            stockMovement.setItem(item);
            stockMovement.setQuantity(stockMovementDTO.getQuantity());
            stockMovement.setCreationDate(LocalDateTime.now());
        }
        return stockMovementRepository.save(stockMovement);
    }

    @Override
    public Optional<StockMovement> getStockMovementById(Long id) {
        return stockMovementRepository.findById(id);
    }

    @Override
    public List<StockMovement> getAllStockMovements() {
        return stockMovementRepository.findAll();
    }

    @Override
    public StockMovement updateStockMovement(Long id, StockMovementDTO stockMovementDTO) {
        StockMovement stockMovement = new StockMovement();
        Optional<StockMovement> existingStockMovement = getStockMovementById(id);
        if (existingStockMovement.isPresent()) {
            Item item = new Item();
            item.setId(existingStockMovement.get().getItem().getId());
            item.setName(existingStockMovement.get().getItem().getName());
            stockMovement.setItem(item);
            stockMovement.setId(existingStockMovement.get().getId());
            Integer quantity = stockMovementDTO.getQuantity();
            if (quantity == null) { throw new IllegalArgumentException("Quantity must not be null");}
            if (quantity < 0) { throw new IllegalArgumentException("Quantity value must not be negative");}
            stockMovement.setQuantity(existingStockMovement.get().getQuantity() + quantity);


            stockMovement.setCreationDate(existingStockMovement.get().getCreationDate());
        }
        stockMovementRepository.save(stockMovement);

        List<Order> ordersPending = orderRepository.findOrderByStatusAndItemId(stockMovement.getItem().getId());
        for (Order order : ordersPending) {
            order.setStatus("Done");
            orderRepository.save(order);
        }
        return stockMovement;
    }

    @Override
    public void deleteStockMovement(Long id) {
        stockMovementRepository.deleteById(id);
    }

    public StockMovement findStockMovementByItemName(String itemName) {
        // TODO: Refact to JPA query
        StockMovement stockMovement = new StockMovement();
        List<StockMovement> stockMovements = getAllStockMovements();
        for (StockMovement findStockMovement : stockMovements) {
            if (findStockMovement.getItem().getName().equals(itemName)) {
                stockMovement = findStockMovement;
            }
        }
        return stockMovement;
    }

    public Boolean existsStockMovementByItemName(String itemName) {
        // TODO: Refact to JPA query
        List<StockMovement> stockMovements = getAllStockMovements();
        for (StockMovement stockMovement: stockMovements) {
            if(stockMovement.getItem().getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }
}
