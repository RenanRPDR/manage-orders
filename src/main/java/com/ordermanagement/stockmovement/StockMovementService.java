package com.ordermanagement.stockmovement;

import com.ordermanagement.email.EmailController;
import com.ordermanagement.email.EmailDTO;
import com.ordermanagement.item.Item;
import com.ordermanagement.item.ItemService;
import com.ordermanagement.order.Order;
import com.ordermanagement.order.OrderRepository;
import com.ordermanagement.stock.IStockRepository;
import com.ordermanagement.stock.Stock;
import com.ordermanagement.stock.StockService;
import com.ordermanagement.user.User;
import com.ordermanagement.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockMovementService implements IStockMovementService  {

    private final ItemService itemService;
    private final OrderRepository orderRepository;
    private final EmailController emailController;
    private final UserRepository userRepository;
    private final StockService stockService;
    private final IStockRepository iStockRepository;

    @Autowired
    private final IStockMovementRepository stockMovementRepository;

    public StockMovementService(IStockMovementRepository stockMovementRepository,
                                ItemService itemService,
                                OrderRepository orderRepository,
                                EmailController emailController,
                                UserRepository userRepository, StockService stockService, IStockRepository iStockRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemService = itemService;
        this.orderRepository = orderRepository;
        this.emailController = emailController;
        this.userRepository = userRepository;
        this.stockService = stockService;
        this.iStockRepository = iStockRepository;
    }

    @Override
    public StockMovement updateStock(StockMovementDTO stockMovementDTO) {
        //TODO: Realizar validacao do stockMovementDTO
        // implement...

        Optional<Stock> optionalStockById = stockService.getStockById(stockMovementDTO.getStockId());
        if (optionalStockById.isEmpty()){ throw new Error("Stock does not exist"); }

        Stock stock = new Stock();
        Integer quantityFromStockMovement = stockMovementDTO.getQuantity();
        Integer quantityFromStock = optionalStockById.get().getQuantity();

        switch (stockMovementDTO.getStockOperation()) {
            case "put":
                if (quantityFromStockMovement > 0) {
                    stock.setQuantity(quantityFromStock + quantityFromStockMovement);
                    stock.setId(optionalStockById.get().getId());
                    stock.setItem(optionalStockById.get().getItem());
                    stock.setCreationDate(LocalDateTime.now());
                    iStockRepository.save(stock);
                } else {
                    throw new IllegalArgumentException("You must add a number greater than zero.");
                }
                break;
            case "withdraw":
                if (quantityFromStock >= quantityFromStockMovement) {
                    stock.setQuantity(quantityFromStock - quantityFromStockMovement);
                    stock.setQuantity(stockMovementDTO.getQuantity());
                    stock.setId(optionalStockById.get().getId());
                    stock.setItem(optionalStockById.get().getItem());
                    stock.setCreationDate(LocalDateTime.now());
                    iStockRepository.save(stock);
                } else {
                    throw new IllegalArgumentException("Not enough stock.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid stock operation. Use put or withdraw in stockOperation.");
        }

        StockMovement stockMovement = new StockMovement();
        stockMovement.setStock(stock);
        stockMovement.setQuantity(stockMovementDTO.getQuantity());
        stockMovement.setItem(stock.getItem());
        stockMovement.setCreationDate(LocalDateTime.now());
        return stockMovementRepository.save(stockMovement);
    }

    @Override
    public StockMovement createStockMovement(StockMovementDTO stockMovementDTO) {
        Long itemId = stockMovementDTO.getItemId();
        Optional<Item> optionalItem = itemService.getItemById(itemId);
        if (optionalItem.isEmpty()){
            throw new Error("Item does not exist");
        }

        Boolean stockMovementForThisItem = existsStockMovementByItemName(optionalItem.get().getName());
        if (stockMovementForThisItem){
            throw new Error("Does exist stockmovement for this item");
        }

        StockMovement stockMovement = new StockMovement();
        if (!stockMovementForThisItem) {
            Item item = new Item();
            item.setId(optionalItem.get().getId());
            item.setName(optionalItem.get().getName());

            stockMovement.setItem(item);
            stockMovement.setQuantity(stockMovementDTO.getQuantity());
            stockMovement.setCreationDate(LocalDateTime.now());
        }
        return stockMovementRepository.save(stockMovement);
    }

    @Override
    public Optional<StockMovement> getStockMovementById(Long id) {
        return stockMovementRepository.findById(id);
    }

    @Override
    public List<StockMovement> getAllStockMovements() {
        return stockMovementRepository.findAll();
    }

    @Override
    public StockMovement update(Long id, StockMovementDTO stockMovementDTO) {
        StockMovement stockMovement = new StockMovement();
        Optional<StockMovement> existingStockMovement = getStockMovementById(id);
        if (existingStockMovement.isPresent()) {
            Item item = new Item();
            item.setId(existingStockMovement.get().getItem().getId());
            item.setName(existingStockMovement.get().getItem().getName());
            stockMovement.setItem(item);
            stockMovement.setId(existingStockMovement.get().getId());
            Integer quantity = stockMovementDTO.getQuantity();
            if (quantity == null) { throw new IllegalArgumentException("Quantity must not be null");}
            if (quantity < 0) { throw new IllegalArgumentException("Quantity value must not be negative");}
            stockMovement.setQuantity(existingStockMovement.get().getQuantity() + quantity);


            stockMovement.setCreationDate(existingStockMovement.get().getCreationDate());
        }
        stockMovementRepository.save(stockMovement);

        // Trazer uma lista das Orders com: status: "Pending" e item.name = do updateStockMovement
        List<Order> ordersPending = orderRepository.findOrderByStatusAndItemId(stockMovement.getItem().getId());
        EmailDTO emailDTO = new EmailDTO();
        for (Order order : ordersPending) {
            Optional<User> user = userRepository.findById(order.getUser().getId());
            //Disparar o email
            emailDTO.setEmailTo(order.getUser().getEmail());
            // Preenche DTO
            emailDTO = createDtoToSendEmail(order.getUser(), emailDTO);
            emailController.sendEmail(emailDTO);
            order.setStatus("Done");
            orderRepository.save(order);
        }
        return stockMovement;
    }

    @Override
    public void deleteStockMovement(Long id) {
        stockMovementRepository.deleteById(id);
    }

    public StockMovement findStockMovementByItemName(String itemName) {
        // TODO: Refact to JPA query
        StockMovement stockMovement = new StockMovement();
        List<StockMovement> stockMovements = getAllStockMovements();
        for (StockMovement findStockMovement : stockMovements) {
            if (findStockMovement.getItem().getName().equals(itemName)) {
                stockMovement = findStockMovement;
            }
        }
        return stockMovement;
    }

    public Boolean existsStockMovementByItemName(String itemName) {
        //TODO: Refactor to JpaRepository
        List<StockMovement> stockMovements = getAllStockMovements();
        for (StockMovement stockMovement: stockMovements) {
            if(stockMovement.getItem().getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    private static EmailDTO createDtoToSendEmail(User user, EmailDTO emailDTO) {
        emailDTO.setOwnerRef("Order Manegement");
        emailDTO.setEmailFrom("ordermanegementchallenge@gmail.com");
        emailDTO.setSubject("Order done BY StockMovementService");
        emailDTO.setText("Your order is created.");
        return emailDTO;
    }
}
