package com.swiggone.service;

import com.swiggone.dto.FoodOrderRequest;
import com.swiggone.exception.ResourceNotFoundException;
import com.swiggone.model.Customer;
import com.swiggone.model.FoodItem;
import com.swiggone.model.FoodOrder;
import com.swiggone.repository.CustomerRepository;
import com.swiggone.repository.FoodItemRepository;
import com.swiggone.repository.FoodOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final CustomerRepository customerRepository;
    private final FoodItemRepository foodItemRepository;

    public FoodOrderService(FoodOrderRepository foodOrderRepository,
                            CustomerRepository customerRepository,
                            FoodItemRepository foodItemRepository) {
        this.foodOrderRepository = foodOrderRepository;
        this.customerRepository = customerRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<FoodOrder> getAllOrders() {
        return foodOrderRepository.findAll();
    }

    public FoodOrder getOrderById(Integer id) {
        return foodOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order " + id + " was not found"));
    }

    public FoodOrder placeOrder(FoodOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer " + request.getCustomerId() + " was not found"));

        FoodItem foodItem = foodItemRepository.findById(request.getFoodItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Food item " + request.getFoodItemId() + " was not found"));

        if (!foodItem.isAvailable()) {
            throw new IllegalArgumentException("This food item is not available right now");
        }

        FoodOrder order = new FoodOrder();
        order.setCustomer(customer);
        order.setFoodItem(foodItem);
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(foodItem.getPrice() * request.getQuantity());
        order.setStatus(request.getStatus() == null ? "PLACED" : request.getStatus());

        return foodOrderRepository.save(order);
    }

    public FoodOrder updateOrder(Integer id, FoodOrderRequest request) {
        FoodOrder existingOrder = getOrderById(id);

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer " + request.getCustomerId() + " was not found"));

        FoodItem foodItem = foodItemRepository.findById(request.getFoodItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Food item " + request.getFoodItemId() + " was not found"));

        if (!foodItem.isAvailable()) {
            throw new IllegalArgumentException("This food item is not available right now");
        }

        existingOrder.setCustomer(customer);
        existingOrder.setFoodItem(foodItem);
        existingOrder.setQuantity(request.getQuantity());
        existingOrder.setTotalAmount(foodItem.getPrice() * request.getQuantity());

        if (request.getStatus() != null) {
            existingOrder.setStatus(request.getStatus());
        }

        return foodOrderRepository.save(existingOrder);
    }

    public void deleteOrder(Integer id) {
        FoodOrder order = getOrderById(id);
        foodOrderRepository.delete(order);
    }
}
