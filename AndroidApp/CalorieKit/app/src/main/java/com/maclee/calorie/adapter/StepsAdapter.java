package com.maclee.calorie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maclee.calorie.R;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.model.Consume;
import com.maclee.calorie.model.Steps;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Steps> mData;


    public StepsAdapter(final Context context) {
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
    public Steps getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getSid();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_steps, parent,false);
            viewHolder.tvSteps = convertView.findViewById(R.id.tv_step);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_step_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSteps.setText(String.valueOf(mData.get(position).getSteps()));
        try {
            viewHolder.tvTime.setText(Toolkit.getFormatDataTime(mData.get(position).getAddTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public List<Steps> getData() {
        return mData;
    }

    class ViewHolder {
        TextView tvSteps;
        TextView tvTime;
    }
}
