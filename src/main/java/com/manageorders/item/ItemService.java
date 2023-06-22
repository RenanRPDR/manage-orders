package com.manageorders.item;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ItemService implements IItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    @Override
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
    @Override
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    @Override
    public Item updateItem(Long id, Item item) {
        return itemRepository.save(item);
    }
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
