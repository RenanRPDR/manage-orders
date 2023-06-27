package com.manageorders.stockmovement;

import com.manageorders.item.Item;
import com.manageorders.item.ItemRepository;
import com.manageorders.item.ItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockMovementService implements IStockMovementService {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
//    private StockMovementDTO stockMovementDTO;
    private final IStockMovementRepository stockMovementRepository;

    public StockMovementService(IStockMovementRepository stockMovementRepository, ItemRepository itemRepository, ItemService itemService) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
//        this.stockMovementDTO = stockMovementDTO;
    }

    @Override
    public StockMovement createStockMovement(StockMovementDTO stockMovementDTO) {
//        String itemNameFromDTO = stockMovementDTO.getItemName();
//        if (findStockMovementByItemName(itemNameFromDTO) == true) {
//            throw new IllegalArgumentException("Already exists stockmovement to this item.");
//        }

//        List<StockMovement> stockMovements = getAllStockMovements();
//        for(StockMovement stockMovement : stockMovements) {
//            stockMovement.getItem().getName() == stockMovementDTO.getItemName();
//        }

        Long itemId = stockMovementDTO.getItemId();
        if (itemRepository.existsById(itemId) == true && findStockMovementByItemName(stockMovementDTO.getItemName()) == false) {
            Optional<Item> optionalItem = itemService.getItemById(itemId);

            Item item = new Item();
            item.setId(optionalItem.get().getId());
            item.setName(optionalItem.get().getName());

            StockMovement stockMovement = new StockMovement();
            stockMovement.setItem(item);
            stockMovement.setQuantity(stockMovementDTO.getQuantity());
            stockMovement.setCreationDate(LocalDateTime.now());

            return stockMovementRepository.save(stockMovement);
        } else {
            throw new IllegalArgumentException("Item does not exist");
        }
    }

    public Boolean findStockMovementByItemName(String itemName) {
        Boolean stockExists = false;

        List<StockMovement> stockMovements = getAllStockMovements();

        for (StockMovement stockMovement : stockMovements) {
            String stockMovementItemName = stockMovement.getItem().getName();
            if (stockMovementItemName.equals(itemName)) {
                stockExists = true;
                break;
            }
        }

        return stockExists;
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
    public StockMovement updateStockMovement(Long id, StockMovement stockMovement) {
        return stockMovementRepository.save(stockMovement);
    }

    @Override
    public void deleteStockMovement(Long id) {
        stockMovementRepository.deleteById(id);
    }
}
