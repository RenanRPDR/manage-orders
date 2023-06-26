package com.manageorders.stockmovement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IStockMovementRepository extends JpaRepository<StockMovement, Long> {
}