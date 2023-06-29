package com.ordermanagement.stockmovement;

import java.util.List;
import java.util.Optional;

public interface IStockMovementService {
    StockMovement createStockMovement(StockMovementDTO stockMovementDTO);
    Optional<StockMovement> getStockMovementById(Long id);
    List<StockMovement> getAllStockMovements();
    StockMovement updateStockMovement(Long id, StockMovement stockMovement);
    void deleteStockMovement(Long id);
}
