package com.ordermanagement.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByItemId(Long itemId);
    @Query(value = "SELECT * FROM orders u WHERE u.status = 'Pending' AND item_id = :itemId", nativeQuery = true)
    List<Order> findOrderByStatusAndItemId(@Param("itemId") Long itemId);
}
