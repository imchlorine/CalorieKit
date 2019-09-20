package com.maclee.calorie.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maclee.calorie.BuildConfig;
import com.maclee.calorie.FoodSearchActivity;
import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;
import com.maclee.calorie.adapter.FoodConsumedAdapter;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.model.Consume;
import com.maclee.calorie.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDietFragment extends Fragment {

    View view;
    FoodConsumedAdapter adapter;
    ListView consumedList;
    Spinner category, foodItem;
    EditText etQuantity;
    Button btnAddConsumed;
    List<Food> foodList;
    Food selected;
    int quantity;
    final String URL_COMSUMED = BuildConfig.SERVER_URL + "consumption/findByUserID/";
    final String URL_CATEGORY = BuildConfig.SERVER_URL + "food/findByCategory/";
    final String URL_ADD_CONSUMED = BuildConfig.SERVER_URL + "consumption";

    public MyDietFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_diet, container, false);

        foodList = new ArrayList();
        adapter = new FoodConsumedAdapter(getContext());
        consumedList = view.findViewById(R.id.list_consumed_food);
        consumedList.setAdapter(adapter);
        category = view.findViewById(R.id.food_category);
        category.getBackground().setColorFilter(getResources().getColor(R.color.quantum_greywhite1000), PorterDuff.Mode.SRC_ATOP);
        foodItem = view.findViewById(R.id.food_item);
        foodItem.getBackground().setColorFilter(getResources().getColor(R.color.quantum_greywhite1000), PorterDuff.Mode.SRC_ATOP);
        addItemsOnSpinnerCategory();
        Object dataTransfer[] = new Object[1];
        try {
            dataTransfer[0] = ((MainActivity) getActivity()).infoObj.getString("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String consumed = "";
        JSONArray resultArray = null;
        JSONObject dataObj = null;
        ArrayList<Consume> consumes = new ArrayList<>();
        try {
            consumed = new ConsumedList().execute(dataTransfer).get();
            resultArray = new JSONArray(consumed);

            for (int i = resultArray.length() - 1 ; i >= 0 ; i--) {
                dataObj = resultArray.getJSONObject(i);
                Consume consume = new Consume();
                consume.setConsTime(dataObj.getString("consdate"));
                consume.setQuantity(dataObj.getString("quantity"));
                String foodInfo = dataObj.getString("foodid");
                JSONObject foodObj = new JSONObject(foodInfo);
                consume.setFoodName(foodObj.getString("foodname"));
                consume.setFoodCalorie(foodObj.getString("calories"));
                consume.setFoodFat(foodObj.getString("fat"));
                consumes.add(consume);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resetDataSource(consumes);
        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), FoodSearchActivity.class);
                startActivity(intent);

                //goToFragment(new AddFoodFragment(),true,"AddFoodFragment");
                getActivity().overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
            }
        });
        etQuantity = view.findViewById(R.id.et_food_quantity);
        etQuantity.setText("1");
        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               enableAddButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAddConsumed = view.findViewById(R.id.btn_add_consumed);
        btnAddConsumed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object dataTransfer[] = new Object[4];
                dataTransfer[0] = Toolkit.getCurrentDataTime();
                dataTransfer[1] = new Gson().toJson(selected);
                dataTransfer[2] = String.valueOf(quantity);
                dataTransfer[3] = ((MainActivity)getActivity()).infoObj.toString() ;
                Log.e("id", dataTransfer[3].toString());

                String createResult = null;
                try {
                  String result =  createResult = new AddConsumed().execute(dataTransfer).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (createResult == "ok"){
                    Toast.makeText(getActivity(),
                            "Add Success!", Toast.LENGTH_LONG).show();
                }
                reload();

            }
        });


        return view;
    }

    public void resetDataSource(ArrayList<Consume> source) {
        adapter.getData().clear();
        adapter.getData().addAll(source);
        adapter.notifyDataSetChanged();
        consumedList.setSelection(0);
    }


    public class ConsumedList extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {
            String userid = (String) objects[0];
            return new HttpUrlHelper().getHttpUrlConnection(URL_COMSUMED + userid);
        }
    }

    public void addItemsOnSpinnerCategory() {
        String[] values = {"Category", "Bread", "Cake", "Drink", "Fruit", "Meat", "Snack", "Vegies", "Other"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.WHITE);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodList.removeAll(foodList);
                foodList.add(new Food("Food"));
                String selected = parent.getItemAtPosition(position).toString();
                Object dataTransfer[] = new Object[1];
                dataTransfer[0] = URL_CATEGORY + selected;
                try {
                    String foodString = new SearchFoodByCategory().execute(dataTransfer).get();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Food>>() {
                    }.getType();
                    List<Food> foods = gson.fromJson(foodString, type);
                    for (Food food : foods) {
                        foodList.add(food);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addItemsOnSpinnerFood(foodList);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void addItemsOnSpinnerFood(List<Food> list) {
        ArrayAdapter<Food> adapter = new ArrayAdapter<Food>(this.getActivity(), android.R.layout.simple_spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.WHITE);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        foodItem.setAdapter(adapter);
        foodItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                selected = (Food) parent.getItemAtPosition(position);
                enableAddButton();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }



    public class SearchFoodByCategory extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... objects) {
            String url = (String) objects[0];
            String data = new HttpUrlHelper().getHttpUrlConnection(url);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    public void enableAddButton(){
        try{
            quantity = Integer.parseInt(etQuantity.getText().toString());
            if(selected.getFoodid() == 0 || quantity <= 0 ){
                btnAddConsumed.setEnabled(false);
            } else {
                btnAddConsumed.setEnabled(true);
            }
        }catch(NumberFormatException ex){
        }catch (NullPointerException ex){}


    }

    public class AddConsumed extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... objects) {
            String consdate = (String) objects[0];
            String foodid = (String) objects[1];
            String quantity = (String) objects[2];
            String userid = (String) objects[3];
            String jsonString = "{\"consdate\": \"" + consdate
                    + "\"" + "," + "\"foodid\": " + foodid
                     + "," + "\"quantity\":" + quantity
                    + "," + "\"userid\": " + userid + "}";
            Log.w("JsonString", jsonString);
            String data = new HttpUrlHelper().getHttpUrlPostConnection(URL_ADD_CONSUMED, jsonString);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void reload(){
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("MyDietFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    void goToFragment(Fragment fragment, boolean isTop, String TAG) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, TAG);
        // fragmentTransaction.add(fragment,"Bookmark");
        if (!isTop) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
