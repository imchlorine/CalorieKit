package com.maclee.calorie.model;

public class Food {
    private int foodid;
    private String foodname;
    private String category;
    private int calories;
    private String servingunit;
    private double servingamount;
    private double fat;

    public Food() {
    }

    public Food(String foodname){
        this.foodname = foodname;
    }

    public Food(int foodid, String foodname, String category, int calories, String servingunit, double servingamount, int fat) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.category = category;
        this.calories = calories;
        this.servingunit = servingunit;
        this.servingamount = servingamount;
        this.fat = fat;
    }

    public int getFoodid() {
        return foodid;
    }

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getServingunit() {
        return servingunit;
    }

    public void setServingunit(String servingunit) {
        this.servingunit = servingunit;
    }

    public double getServingamount() {
        return servingamount;
    }

    public void setServingamount(double servingamount) {
        this.servingamount = servingamount;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    @Override
    public String toString() {
        return foodname;
    }

}
