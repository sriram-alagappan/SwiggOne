package com.swiggone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class FoodOrderRequest {

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Food item ID is required")
    private Integer foodItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Pattern(
        regexp = "PLACED|PREPARING|OUT_FOR_DELIVERY|DELIVERED|CANCELLED",
        message = "Status must be PLACED, PREPARING, OUT_FOR_DELIVERY, DELIVERED or CANCELLED"
    )
    private String status = "PLACED";

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Integer foodItemId) {
        this.foodItemId = foodItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
