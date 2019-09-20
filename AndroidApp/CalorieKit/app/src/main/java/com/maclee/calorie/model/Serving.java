package com.maclee.calorie.model;

import java.math.BigDecimal;

public class Serving {
    private Long serving_id;
    private String serving_description;
    private String serving_url;
    private BigDecimal metric_serving_amount;
    private String metric_serving_unit;
    private BigDecimal number_of_units;
    private String measurement_description;
    private BigDecimal calories;
    private BigDecimal carbohydrate;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal saturated_fat;
    private BigDecimal polyunsaturated_fat;
    private BigDecimal monounsaturated_fat;
    private BigDecimal trans_fat;
    private BigDecimal cholesterol;
    private BigDecimal sodium;
    private BigDecimal potassium;
    private BigDecimal fiber;
    private BigDecimal sugar;
    private BigDecimal vitamin_a;
    private BigDecimal vitamin_c;
    private BigDecimal calcium;
    private BigDecimal iron;

    public Serving() {
    }

    public Long getServing_id() {
        return serving_id;
    }

    public void setServing_id(Long serving_id) {
        this.serving_id = serving_id;
    }

    public String getServing_description() {
        return serving_description;
    }

    public void setServing_description(String serving_description) {
        this.serving_description = serving_description;
    }

    public String getServing_url() {
        return serving_url;
    }

    public void setServing_url(String serving_url) {
        this.serving_url = serving_url;
    }

    public BigDecimal getMetric_serving_amount() {
        return metric_serving_amount;
    }

    public void setMetric_serving_amount(BigDecimal metric_serving_amount) {
        this.metric_serving_amount = metric_serving_amount;
    }

    public String getMetric_serving_unit() {
        return metric_serving_unit;
    }

    public void setMetric_serving_unit(String metric_serving_unit) {
        this.metric_serving_unit = metric_serving_unit;
    }

    public BigDecimal getNumber_of_units() {
        return number_of_units;
    }

    public void setNumber_of_units(BigDecimal number_of_units) {
        this.number_of_units = number_of_units;
    }

    public String getMeasurement_description() {
        return measurement_description;
    }

    public void setMeasurement_description(String measurement_description) {
        this.measurement_description = measurement_description;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public void setCalories(BigDecimal calories) {
        this.calories = calories;
    }

    public BigDecimal getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(BigDecimal carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(BigDecimal saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public BigDecimal getPolyunsaturated_fat() {
        return polyunsaturated_fat;
    }

    public void setPolyunsaturated_fat(BigDecimal polyunsaturated_fat) {
        this.polyunsaturated_fat = polyunsaturated_fat;
    }

    public BigDecimal getMonounsaturated_fat() {
        return monounsaturated_fat;
    }

    public void setMonounsaturated_fat(BigDecimal monounsaturated_fat) {
        this.monounsaturated_fat = monounsaturated_fat;
    }

    public BigDecimal getTrans_fat() {
        return trans_fat;
    }

    public void setTrans_fat(BigDecimal trans_fat) {
        this.trans_fat = trans_fat;
    }

    public BigDecimal getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(BigDecimal cholesterol) {
        this.cholesterol = cholesterol;
    }

    public BigDecimal getSodium() {
        return sodium;
    }

    public void setSodium(BigDecimal sodium) {
        this.sodium = sodium;
    }

    public BigDecimal getPotassium() {
        return potassium;
    }

    public void setPotassium(BigDecimal potassium) {
        this.potassium = potassium;
    }

    public BigDecimal getFiber() {
        return fiber;
    }

    public void setFiber(BigDecimal fiber) {
        this.fiber = fiber;
    }

    public BigDecimal getSugar() {
        return sugar;
    }

    public void setSugar(BigDecimal sugar) {
        this.sugar = sugar;
    }

    public BigDecimal getVitamin_a() {
        return vitamin_a;
    }

    public void setVitamin_a(BigDecimal vitamin_a) {
        this.vitamin_a = vitamin_a;
    }

    public BigDecimal getVitamin_c() {
        return vitamin_c;
    }

    public void setVitamin_c(BigDecimal vitamin_c) {
        this.vitamin_c = vitamin_c;
    }

    public BigDecimal getCalcium() {
        return calcium;
    }

    public void setCalcium(BigDecimal calcium) {
        this.calcium = calcium;
    }

    public BigDecimal getIron() {
        return iron;
    }

    public void setIron(BigDecimal iron) {
        this.iron = iron;
    }
}
