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

    private StockMovementDTO stockMovementDTO;
    private final IStockMovementRepository stockMovementRepository;

    public StockMovementService(IStockMovementRepository stockMovementRepository, ItemRepository itemRepository, ItemService itemService, StockMovementDTO stockMovementDTO) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.stockMovementDTO = stockMovementDTO;
    }

    @Override
    public StockMovementDTO createStockMovement(StockMovementDTO stockMovementDTO) {

        if (itemRepository.existsById(stockMovementDTO.getItemId())) {
            Optional<Item> item = itemService.getItemById(itemIdFromUrl);

            //            stockMovementDTO.setItemId(item.get().getId());
            //            stockMovement.setItem();

            stockMovement.setQuantity(stockMovement.getQuantity());

            return stockMovementRepository.save(stockMovement);
        } else {
            throw new IllegalArgumentException("Item does not exist");
        }
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
