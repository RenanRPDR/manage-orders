package com.ordermanagement.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IStockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "SELECT * FROM stocks u WHERE u.item_id = :itemId", nativeQuery = true)
    Stock findStockByItemId(@Param("itemId") Long itemId);
}