package com.ordermanagement.stockmovement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStockMovementRepository extends JpaRepository<StockMovement, Long> {
    // TODO: implement.
    public List<StockMovement> findByItemName(String itemName);
}