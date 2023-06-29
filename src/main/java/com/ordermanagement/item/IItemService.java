package com.ordermanagement.item;

import java.util.List;
import java.util.Optional;

public interface IItemService {
        Item createItem(Item item);
        Optional<Item> getItemById(Long id);
        List<Item> getAllItems();
        Item updateItem(Long id, Item item);
        void deleteItem(Long id);
}
