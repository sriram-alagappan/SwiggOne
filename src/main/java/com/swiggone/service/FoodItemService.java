package com.swiggone.service;

import com.swiggone.exception.ResourceNotFoundException;
import com.swiggone.model.FoodItem;
import com.swiggone.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public FoodItem getFoodItemById(Integer id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Food item " + id + " was not found"));
    }

    public FoodItem addFoodItem(FoodItem foodItem) {
        // The database should generate the ID for a new item.
        foodItem.setId(null);
        return foodItemRepository.save(foodItem);
    }

    public FoodItem updateFoodItem(Integer id, FoodItem updatedItem) {
        FoodItem existingItem = getFoodItemById(id);

        existingItem.setName(updatedItem.getName());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setAvailable(updatedItem.isAvailable());

        return foodItemRepository.save(existingItem);
    }

    public void deleteFoodItem(Integer id) {
        FoodItem foodItem = getFoodItemById(id);
        foodItemRepository.delete(foodItem);
    }
}
