package com.maclee.calorie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.maclee.calorie.adapter.FoodSearchAdapter;
import com.maclee.calorie.api.FatSecretApi;
import com.maclee.calorie.fragment.AddFoodFragment;
import com.maclee.calorie.listener.RecyclerItemClickListener;
import com.maclee.calorie.model.CompactFood;

import java.lang.reflect.Field;
import java.util.List;

public class FoodSearchActivity extends AppCompatActivity {

    SearchView searchView;
    FoodSearchAdapter adapter;
    RecyclerView rvFoodList;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
   // AddFoodFragment addFoodFragment;

    FrameLayout progressBarHolder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(null);
        //addFoodFragment = new AddFoodFragment();
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        rvFoodList = findViewById(R.id.rv_search_list);
        rvFoodList.setHasFixedSize(true);
        rvFoodList.setItemViewCacheSize(20);
        rvFoodList.setDrawingCacheEnabled(true);
        rvFoodList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvFoodList.addItemDecoration(new DividerItemDecoration(rvFoodList.getContext(), DividerItemDecoration.VERTICAL));

        searchView = findViewById(R.id.search_view);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("submit",s);
                Object dataTransfer[] = new Object[1];
                dataTransfer[0] = s;
                new SearchFood(FoodSearchActivity.this).execute(dataTransfer);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        rvFoodList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rvFoodList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                       long itemId = adapter.getItemId(position);
                        //Toast.makeText(FoodSearchActivity.this,String.valueOf(itemId), Toast.LENGTH_LONG).show();
                        goToFoodDetail(itemId);
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //click on back arrow
            case android.R.id.home:
                this.finish();
                overridePendingTransition(
                        R.anim.trans_top_in, R.anim.trans_top_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
            v.clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public class SearchFood extends AsyncTask<Object, String, List<CompactFood>> {

        Context context;

        public SearchFood(Context c) {
            this.context = c;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CompactFood> doInBackground(Object... objects) {
            //new GoogleSearchEngine().searchKeywords("apple");
            String query = (String) objects[0];
            try {
                return new FatSecretApi().getFoodInfo(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<CompactFood> compactFoods) {
            super.onPostExecute(compactFoods);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            // Attach the adapter to the recyclerview to populate items
            adapter = new FoodSearchAdapter(context, compactFoods);
            rvFoodList.setAdapter(adapter);
            // Set layout manager to position the items
            rvFoodList.setLayoutManager(new LinearLayoutManager(context));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }




    public void goToFoodDetail(long id){
        Intent intent = new Intent(FoodSearchActivity.this,FoodDetailActivity.class);
        intent.putExtra("FoodId", id);
        startActivity(intent);
    }

}
