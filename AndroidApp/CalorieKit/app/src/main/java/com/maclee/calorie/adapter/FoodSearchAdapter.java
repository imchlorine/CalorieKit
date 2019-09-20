package com.maclee.calorie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maclee.calorie.R;
import com.maclee.calorie.model.CompactFood;

import java.util.List;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.ViewHolder>  {
    private Context mContext;
    private List<CompactFood> mData;

    public FoodSearchAdapter(Context context, List<CompactFood> food) {
        this.mContext = context;
        this.mData = food;
    }

    @Override
    public FoodSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.food_list_layout, parent, false);
        FoodSearchAdapter.ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(FoodSearchAdapter.ViewHolder viewHolder, int position) {
        CompactFood compactFood = mData.get(position);
        // Set item views based on your views and data model
        TextView tvFoodName = viewHolder.tvFoodName;
        tvFoodName.setText(compactFood.getFood_name());
        TextView tvFoodDesc = viewHolder.tvFoodDesc;
        tvFoodDesc.setText(compactFood.getFood_description());
    }


    @Override
    public long getItemId(int position) {
        CompactFood compactFood = mData.get(position);
        return compactFood.getFood_id();
    }

    @Override
    public int getItemCount() {
        if (mData!=null){
            return  mData.size();

        }
        else {
            return 0;
        }


    }

    //View holder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFoodName;
        public TextView tvFoodDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = (TextView) itemView.findViewById(R.id.tv_food_name);
            tvFoodDesc = (TextView) itemView.findViewById(R.id.tv_food_desc);
        }


    }



}
