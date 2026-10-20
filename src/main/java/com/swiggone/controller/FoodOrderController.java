package com.swiggone.controller;

import com.swiggone.dto.FoodOrderRequest;
import com.swiggone.model.FoodOrder;
import com.swiggone.service.FoodOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    public FoodOrderController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return foodOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public FoodOrder getOrderById(@PathVariable Integer id) {
        return foodOrderService.getOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodOrder placeOrder(@Valid @RequestBody FoodOrderRequest request) {
        return foodOrderService.placeOrder(request);
    }

    @PutMapping("/{id}")
    public FoodOrder updateOrder(@PathVariable Integer id,
                                 @Valid @RequestBody FoodOrderRequest request) {
        return foodOrderService.updateOrder(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Integer id) {
        foodOrderService.deleteOrder(id);
    }
}
