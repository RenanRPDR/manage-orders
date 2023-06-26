package com.manageorders.stockmovement;

import com.manageorders.item.Item;

import java.util.List;
import java.util.Optional;

public interface IStockMovementService {
    StockMovement createStockMovement(Long itemIdFromUrl, StockMovement stockMovement);
    Optional<StockMovement> getStockMovementById(Long id);
    List<StockMovement> getAllStockMovements();
    StockMovement updateStockMovement(Long id, StockMovement stockMovement);
    void deleteStockMovement(Long id);
}
