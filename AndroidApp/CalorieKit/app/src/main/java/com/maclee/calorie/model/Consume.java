package com.maclee.calorie.model;

public class Consume {
    private String foodName;
    private String foodCalorie;
    private String foodFat;
    private String consTime;
    private String quantity;

    public Consume() {
    }

    public Consume(String foodName, String foodCalorie, String foodFat, String consTime, String quantity) {
        this.foodName = foodName;
        this.foodCalorie = foodCalorie;
        this.foodFat = foodFat;
        this.consTime = consTime;
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCalorie() {
        return foodCalorie;
    }

    public void setFoodCalorie(String foodCalorie) {
        this.foodCalorie = foodCalorie;
    }

    public String getFoodFat() {
        return foodFat;
    }

    public void setFoodFat(String foodFat) {
        this.foodFat = foodFat;
    }

    public String getConsTime() {
        return consTime;
    }

    public void setConsTime(String consTime) {
        this.consTime = consTime;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
