package com.ordermanagement.stockmovement;

import java.util.List;
import java.util.Optional;

public interface IStockMovementService {
    StockMovement updateStock(StockMovementDTO stockMovementDTO);

    StockMovement createStockMovement(StockMovementDTO stockMovementDTO);
    Optional<StockMovement> getStockMovementById(Long id);
    List<StockMovement> getAllStockMovements();
    StockMovement update(Long id, StockMovementDTO stockMovementDTO);
    void deleteStockMovement(Long id);
}
