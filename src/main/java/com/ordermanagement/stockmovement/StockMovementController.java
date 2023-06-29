package com.ordermanagement.stockmovement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stockmovements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    public ResponseEntity<StockMovement> createStockMovement(@RequestBody StockMovementDTO stockMovementDTO) {
        StockMovement createdStockMovement = stockMovementService.createStockMovement(stockMovementDTO);
        return new ResponseEntity<>(createdStockMovement, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getStockMovement(@PathVariable Long id) {
        Optional<StockMovement> item = stockMovementService.getStockMovementById(id);
        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<StockMovement>> getAllStockMovements() {
        List<StockMovement> items = stockMovementService.getAllStockMovements();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovement> updateStockMovement(@PathVariable Long id, @RequestBody StockMovement item) {
        Optional<StockMovement> existingStockMovement = stockMovementService.getStockMovementById(id);
        if (existingStockMovement.isPresent()) {
            item.setId(id);
            StockMovement updatedStockMovement = stockMovementService.updateStockMovement(id, item);
            return new ResponseEntity<>(updatedStockMovement, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
        Optional<StockMovement> item = stockMovementService.getStockMovementById(id);
        if (item.isPresent()) {
            stockMovementService.deleteStockMovement(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
