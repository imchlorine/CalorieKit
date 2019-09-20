package com.maclee.calorie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maclee.calorie.api.FatSecretApi;
import com.maclee.calorie.api.GoogleSearchEngine;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.model.Food;
import com.maclee.calorie.model.FoodServingList;
import com.maclee.calorie.model.FoodServingList.*;
import com.maclee.calorie.model.Serving;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class FoodDetailActivity extends AppCompatActivity {

    TextView tvFoodName, tvFoodCalorie, tvFoodCal, tvFoodServingUnit, tvFoodServingAmount, tvFoodFat, tvFoodCarb, tvFoodProt, tvFoodDesc;
    Button btnAddFood, btnMoreDetail;
    FoodServingList foodServingList;
    ImageView ivFood;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;
    final String url_AddFood = BuildConfig.SERVER_URL + "food/addFood";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        // Status bar :: Transparent
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setTitle(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#939393"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        tvFoodCarb = findViewById(R.id.tv_food_detail_carb);
        tvFoodCal = findViewById(R.id.tv_food_calorie_detail);
        tvFoodCalorie = findViewById(R.id.tv_food_detail_cal);
        tvFoodDesc = findViewById(R.id.tv_food_detail_desc);
        tvFoodFat = findViewById(R.id.tv_food_detail_fat);
        tvFoodName = findViewById(R.id.tv_food_detail_name);
        tvFoodProt = findViewById(R.id.tv_food_detail_prot);
        tvFoodServingAmount = findViewById(R.id.tv_food_detail_amount);
        tvFoodServingUnit = findViewById(R.id.tv_food_detail_serving);
        btnAddFood = findViewById(R.id.btn_add_food);
        btnMoreDetail = findViewById(R.id.btn_visit_more);
        ivFood = findViewById(R.id.iv_food_detail);

        btnMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(foodServingList.getFood_url());
            }
        });

        //set Action Bar Transparency
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        long foodId = getIntent().getLongExtra("FoodId", 0);
        Object dataTransfer[] = new Object[1];
        dataTransfer[0] = foodId;
        new GetFoodDetail().execute(dataTransfer);

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food();
                Servings servings = foodServingList.getServings();
                Serving serving = servings.getServing().get(0);
                food.setFoodname(tvFoodName.getText().toString());
                food.setCalories(Integer.parseInt(serving.getCalories().toString()));
                food.setFat(Double.parseDouble(serving.getFat().toString()));
                food.setServingamount(Double.parseDouble(serving.getMetric_serving_amount().toString()));
                food.setServingunit(serving.getMetric_serving_unit());
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(FoodDetailActivity.this);
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Select One Category");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FoodDetailActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Bread");
                arrayAdapter.add("Cake");
                arrayAdapter.add("Drink");
                arrayAdapter.add("Fruit");
                arrayAdapter.add("Meat");
                arrayAdapter.add("Snack");
                arrayAdapter.add("Vegies");
                arrayAdapter.add("Other");
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        food.setCategory(arrayAdapter.getItem(which));
                        new AddToFood().execute(food);
                    }
                });
                builderSingle.show();
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //click on back arrow
            case android.R.id.home:
                this.finish();
                overridePendingTransition(
                        R.anim.trans_right_in, R.anim.trans_right_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class GetFoodDetail extends AsyncTask<Object, String, FoodServingList> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }
        @Override
        protected FoodServingList doInBackground(Object... objects) {
            try {
                foodServingList = new FatSecretApi().getFoodById((long) objects[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodServingList;
        }

        @Override
        protected void onPostExecute(FoodServingList compactFood) {
            super.onPostExecute(compactFood);
            Servings servings = compactFood.getServings();
            Serving serving = servings.getServing().get(0);
            tvFoodName.setText(compactFood.getFood_name());
            tvFoodCal.setText(serving.getCalories().toString() + " Calories");
            tvFoodCalorie.setText(serving.getCalories().toString());
            tvFoodFat.setText(serving.getFat().toString());
            tvFoodProt.setText(serving.getProtein().toString());
            if (serving.getCalcium() != null){
                tvFoodCarb.setText(serving.getCalcium().toString());
            }
            else {
                tvFoodCarb.setText("0");
            }

            if (serving.getMetric_serving_amount() != null && serving.getMetric_serving_unit() != null) {
                tvFoodServingAmount.setText("serving amount: " + serving.getMetric_serving_amount().toBigInteger().toString());
                tvFoodServingUnit.setText("serving unit: " + serving.getMetric_serving_unit());
            }else {
                tvFoodServingAmount.setVisibility(View.INVISIBLE);
                tvFoodServingUnit.setText(serving.getServing_description());
            }
            Object dataTransfer[] = new Object[1];
            dataTransfer[0] = compactFood.getFood_name();
            new GetFoodPicAndDesc().execute(dataTransfer);
        }
    }

    public class GetFoodPicAndDesc extends AsyncTask<Object, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... objects) {
            String result = new GoogleSearchEngine().searchKeywords((String) objects[0]);
            JSONObject product = null;
            try {
                JSONArray jsonArray = new JSONObject(result).getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject itemsObject = jsonArray.getJSONObject(i);
                    //JitemsObject = (JSONObject) itemsObject.getJSONObject("pagemap");
                    if (!itemsObject.getJSONObject("pagemap").isNull("product")){
                    JSONObject metatags = (JSONObject) itemsObject.getJSONObject("pagemap").getJSONArray("product").get(0);
                        if (metatags.getString("image").contains("wowproductimages")){
                            product = metatags;
                            break;
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return product;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            Object dataTransfer[] = new Object[1];
            if (s!=null){
            try {
                if (!s.isNull("description")){
                    tvFoodDesc.setText(s.getString("description"));
                }
                else {
                    tvFoodDesc.setText("No description");
                }

                if (!s.isNull("image")){
                    dataTransfer[0] = s.getString("image");
                }else {
                    dataTransfer[0] = "";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }}
            else {
                dataTransfer[0] = "";
            }
            new DownloadImageTask().execute(dataTransfer);
        }
    }

    private void goToUrl(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private class DownloadImageTask extends AsyncTask<Object, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Object... urls) {
            Bitmap mIcon11 = null;
            if (!urls[0].equals("")){
            try {
                InputStream in = new java.net.URL((String) urls[0]).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }}
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result!=null){
            ivFood.setImageBitmap(result);}
            else {
                ivFood.setImageResource(R.drawable.work_outut);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }

    public class AddToFood extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... objects) {
            Gson gson = new Gson();
            String foodString = gson.toJson((Food)objects[0]);

            String data = new HttpUrlHelper().getHttpUrlPostConnection(url_AddFood,foodString);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")){
                Toast.makeText(FoodDetailActivity.this,"Added Success!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(FoodDetailActivity.this,"Added failed!", Toast.LENGTH_LONG).show();
            }
        }
    }


}
