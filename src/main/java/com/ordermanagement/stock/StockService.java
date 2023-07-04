package com.ordermanagement.stock;

import com.ordermanagement.item.Item;
import com.ordermanagement.item.ItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final ItemService itemService;
    private final IStockRepository iStockRepository;

    public StockService(ItemService itemService, IStockRepository iStockRepository) {
        this.itemService = itemService;
        this.iStockRepository = iStockRepository;
    }

    public Optional<Stock> getStockById(Long id) {
        return iStockRepository.findById(id);
    }

    public List<Stock> getAllStocks(){
        return iStockRepository.findAll();
    }

    public Stock createStock(StockDTO stockDTO) {
        Long itemId = stockDTO.getItemId();
        Optional<Item> optionalItem = itemService.getItemById(itemId);
        if (optionalItem.isEmpty()){
            throw new Error("Item does not exist");
        }

        Boolean stockForThisItem = existStockByItemName(optionalItem.get().getName());
        if (stockForThisItem) {
            throw new Error("Does exist stock for this item");
        }

        Stock stock = new Stock();
        if (!stockForThisItem) {
            Item item = new Item();
            item.setId(optionalItem.get().getId());
            item.setName(optionalItem.get().getName());

            stock.setItem(item);
            stock.setQuantity(stockDTO.getQuantity());
            stock.setCreationDate(LocalDateTime.now());
        }
        return iStockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        iStockRepository.deleteById(id);
    }

    public Boolean existStockByItemName(String itemName) {
        //TODO: Refactor to JpaRepository
        List<Stock> stocks = getAllStocks();
        for (Stock stock: stocks) {
            if(stock.getItem().getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }
}
