package com.maclee.calorie.model;

public class FoodSevingSingle extends CompactFood {
    private Servings servings;

    public FoodSevingSingle(){}

    public Servings getServings() {
        return servings;
    }

    public void setServings(Servings servings) {
        this.servings = servings;
    }

    public class Servings{

        private Serving serving;

        public Serving getServing() {
            return serving;
        }

        public void setServing(Serving serving) {
            this.serving = serving;
        }
    }

}
