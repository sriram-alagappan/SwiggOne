package com.swiggone.controller;

import com.swiggone.model.FoodItem;
import com.swiggone.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping
    public List<FoodItem> getAllFoodItems() {
        return foodItemService.getAllFoodItems();
    }

    @GetMapping("/{id}")
    public FoodItem getFoodItemById(@PathVariable Integer id) {
        return foodItemService.getFoodItemById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodItem addFoodItem(@Valid @RequestBody FoodItem foodItem) {
        return foodItemService.addFoodItem(foodItem);
    }

    @PutMapping("/{id}")
    public FoodItem updateFoodItem(@PathVariable Integer id,
                                   @Valid @RequestBody FoodItem foodItem) {
        return foodItemService.updateFoodItem(id, foodItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFoodItem(@PathVariable Integer id) {
        foodItemService.deleteFoodItem(id);
    }
}
