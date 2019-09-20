package com.maclee.calorie.model;

import java.util.List;

public class CompactFood {
    private String food_name;
    private String food_url;
    private String food_type;
    private Long food_id;
    private String food_description;

    public CompactFood() {
    }

    public CompactFood(String food_name, String food_url, String food_type, Long food_id, String food_description) {
        this.food_name = food_name;
        this.food_url = food_url;
        this.food_type = food_type;
        this.food_id = food_id;
        this.food_description = food_description;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_url() {
        return food_url;
    }

    public void setFood_url(String food_url) {
        this.food_url = food_url;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }



}


