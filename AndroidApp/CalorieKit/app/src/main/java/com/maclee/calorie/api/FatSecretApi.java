package com.maclee.calorie.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maclee.calorie.model.CompactFood;
import com.maclee.calorie.model.FoodServingList;
import com.maclee.calorie.model.FoodServingList.Servings;
import com.maclee.calorie.model.FoodSevingSingle;
import com.maclee.calorie.model.Serving;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FatSecretApi {
    private final String KEY = "Your key";
    private final String SECRET = "Your secret";
    FatSecretConnection connection;

    public FatSecretApi() {
        connection = new FatSecretConnection(KEY, SECRET);

    }

    public FoodServingList getFoodById(long id) throws Exception {
        String url = connection.buildFoodGetUrl(id);
        String result = new HttpUrlHelper().getHttpUrlConnection(url);
        JSONObject resultJson = new JSONObject(result);
        String food = resultJson.getString("food");

        Object json = resultJson.getJSONObject("food").getJSONObject("servings").get("serving");
        Gson gson = new Gson();
        FoodServingList compactFood = null;
        if (json instanceof JSONObject) {
            FoodSevingSingle foodSevingSingle = gson.fromJson(food, FoodSevingSingle.class);
            compactFood = new FoodServingList(foodSevingSingle.getFood_name(),foodSevingSingle.getFood_url(),foodSevingSingle.getFood_type(),foodSevingSingle.getFood_id(),foodSevingSingle.getFood_description());
            List<Serving> serving = new ArrayList<>();
            serving.add(foodSevingSingle.getServings().getServing());
            compactFood.setServings(new Servings(serving));
        } else if (json instanceof JSONArray) {
            compactFood = gson.fromJson(food, FoodServingList.class);

        }
        return compactFood;
    }

    public List<CompactFood> getFoodInfo(String query) throws Exception {
        String url = connection.buildFoodsSearchUrl(query, 0);
        String result = new HttpUrlHelper().getHttpUrlConnection(url);
        String jsonArray = new JSONObject(result).getJSONObject("foods").getString("food");
        Type type = new TypeToken<List<CompactFood>>() {
        }.getType();
        Gson gson = new Gson();
        List<CompactFood> listFood = gson.fromJson(jsonArray, type);
        return listFood;
    }


}

