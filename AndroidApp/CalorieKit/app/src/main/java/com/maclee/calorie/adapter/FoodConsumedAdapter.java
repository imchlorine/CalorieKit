package com.maclee.calorie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maclee.calorie.R;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.model.Consume;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FoodConsumedAdapter extends BaseAdapter {

    private Context mContext;
    private List<Consume> mData;


    public FoodConsumedAdapter(final Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }
    @Override
    public int getCount() {

        if (mData!=null){
            return  mData.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_consumed, parent,false);
            viewHolder.foodName = convertView.findViewById(R.id.tv_food_name);
            viewHolder.foodCalorie = convertView.findViewById(R.id.tv_food_calorie);
            viewHolder.foodFat = convertView.findViewById(R.id.tv_food_fat);
            viewHolder.foodDateTime = convertView.findViewById(R.id.tv_food_datetime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.foodName.setText(mData.get(position).getFoodName() + " * " + mData.get(position).getQuantity());
        viewHolder.foodCalorie.setText(mData.get(position).getFoodCalorie() + "cal");
        viewHolder.foodFat.setText("Fat:" + mData.get(position).getFoodFat() + "g");
        try {
            viewHolder.foodDateTime.setText(Toolkit.getFormatDataTime(mData.get(position).getConsTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public List<Consume> getData() {
        return mData;
    }

    class ViewHolder {
        TextView foodName;
        TextView foodCalorie;
        TextView foodFat;
        TextView foodDateTime;
    }
}
