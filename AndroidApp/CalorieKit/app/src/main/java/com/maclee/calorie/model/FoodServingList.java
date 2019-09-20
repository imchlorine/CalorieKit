package com.maclee.calorie.model;

import java.util.List;

public class FoodServingList extends CompactFood {
    Servings servings;

    public FoodServingList(String food_name, String food_url, String food_type, long food_id, String food_description) {
        super(food_name,food_url,food_type,food_id,food_description);
    }

    public Servings getServings() {
        return servings;
    }

    public void setServings(Servings servings) {
        this.servings = servings;
    }

    public static class Servings{

        private List<Serving> serving;

        public Servings(List<Serving> serving) {
            this.serving = serving;
        }

        public List<Serving> getServing() {
            return serving;
        }

        public void setServing(List<Serving> serving) {
            this.serving = serving;
        }
    }
}
